package pl.szczepanik.silencio.processors.visitors;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

import pl.szczepanik.silencio.GenericTest;
import pl.szczepanik.silencio.core.Configuration;
import pl.szczepanik.silencio.core.Execution;
import pl.szczepanik.silencio.decisions.PositiveDecision;
import pl.szczepanik.silencio.mocks.ConverterVisitor;
import pl.szczepanik.silencio.utils.ResourceLoader;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class PropertiesVisitorTest extends GenericTest {

    @Test
    public void shouldVisitAllJsonNodes() throws IOException {

        final int nodesCounter = 16;

        // given
        input = ResourceLoader.loadPropertiesAsReader("suv.properties");
        Properties properties = new Properties();
        properties.load(input);

        PropertiesVisitor visitor = new PropertiesVisitor();
        ConverterVisitor visitCounter = new ConverterVisitor();
        Execution execution = new Execution(new PositiveDecision(), visitCounter);
        visitor.setConfiguration(new Configuration(execution));

        // when
        visitor.process(properties);

        // then
        assertThat(visitCounter.getVisitCounter()).isEqualTo(nodesCounter);
    }
}
