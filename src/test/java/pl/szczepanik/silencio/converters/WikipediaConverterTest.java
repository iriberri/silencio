package pl.szczepanik.silencio.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import pl.szczepanik.silencio.api.Converter;
import pl.szczepanik.silencio.api.Processor;
import pl.szczepanik.silencio.core.Configuration;
import pl.szczepanik.silencio.core.Execution;
import pl.szczepanik.silencio.core.IntegrityException;
import pl.szczepanik.silencio.decisions.PositiveDecision;
import pl.szczepanik.silencio.processors.JSONProcessor;
import pl.szczepanik.silencio.utils.IOUtility;
import pl.szczepanik.silencio.utils.ReflectionUtility;
import pl.szczepanik.silencio.utils.ResourceLoader;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(IOUtility.class)
public class WikipediaConverterTest {

    private static final String URL_ADDRESS = "https://en.m.wikipedia.org/wiki/Special:Random";
    private static final String INVALID_HTML_PAGE = "This does not look like valid HTML page";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Writer output;
    private Reader input;


    @Test
    public void shouldFailWhenServerReturnsInvalidPage() throws IOException {

        // given
        Converter wikipedia = new WikipediaConverter();
        input = ResourceLoader.loadJsonAsReader("suv.json");
        Processor processor = new JSONProcessor();
        Execution execution = new Execution(new PositiveDecision(), wikipedia);
        processor.setConfiguration(new Configuration(execution));
        processor.load(input);

        // when
        mockStatic(IOUtility.class);
        when(IOUtility.urlToString(new URL(URL_ADDRESS)))
            .thenReturn(INVALID_HTML_PAGE);

        // then
        thrown.expect(IntegrityException.class);
        thrown.expectMessage("Could not find header pattern for page: " + INVALID_HTML_PAGE);
        processor.process();
    }

    @Test
    public void shouldConvertWholeFile() throws IOException {

        // given
        Converter converter = new WikipediaConverter();
        input = ResourceLoader.loadJsonAsReader("suv.json");
        output = new StringWriter();
        Processor processor = new JSONProcessor();
        Execution execution = new Execution(new PositiveDecision(), converter);
        processor.setConfiguration(new Configuration(execution));
        processor.load(input);

        // when
        mockStatic(IOUtility.class);
        when(IOUtility.urlToString(new URL(URL_ADDRESS)))
             .thenReturn(toWikiPage("George Washington"))
             .thenReturn(toWikiPage("John Adams"))
             .thenReturn(toWikiPage("George Washington"))
             .thenReturn(toWikiPage("George Washington")) // duplicate to check elimination duplicates
             .thenReturn(toWikiPage("Thomas Jefferson"))
             .thenReturn(toWikiPage("James Madison"))
             .thenReturn(toWikiPageItalics("James Monroe")) // with italics
             .thenReturn(toWikiPage("John Quincy Adams")) // one more time
             .thenReturn(toWikiPage("Andrew Jackson"))
             .thenReturn(toWikiPageItalics("Andrew Jackson")) // with italics
             .thenReturn(toWikiPage("Martin Van Buren"))
             .thenReturn(toWikiPage("William Henry Harrison"))
             .thenReturn(toWikiPage("John Tyler"))
             .thenReturn(toWikiPage("James Polk"))
             .thenReturn(toWikiPage("James Polk"))
             .thenReturn(toWikiPage("Zachary Taylor"))
             .thenThrow(new IllegalArgumentException("Trying to parse more elements than expected!"));

        processor.process();
        processor.write(output);

        // then
        String reference = ResourceLoader.loadJsonAsString("suv_Positive_Wikipedia.json");
        assertThat(output.toString()).isEqualTo(reference);
    }

    @Test
    public void shouldClearHistoryOnInit() {

        // given
        Converter blank = new WikipediaConverter();
        Map<Object, Integer> values = new HashMap<>();
        values.put(this, 0);
        Set<String> words = new HashSet<>();
        words.add(null);

        // when
        ReflectionUtility.setField(blank, "values", values);
        ReflectionUtility.setField(blank, "words", words);
        blank.init();

        // then
        Map<Object, Integer> retValues = (Map) ReflectionUtility.getField(blank, "values");
        assertThat(retValues).isEmpty();
        Set<String> retWords = (Set) ReflectionUtility.getField(blank, "words");
        assertThat(retWords).isEmpty();
    }

    private static String toWikiPage(String text) {
        return String.format("ble bla bla <h1 id=\"section_0\">%s</h1> ble ble ble", text);
    }

    private static String toWikiPageItalics(String text) {
        return String.format("<h1 id=\"section_0\"><i>%s</i></h1> something, something", text);
    }

    @After
    public void closeStreams() {
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);
    }
}
