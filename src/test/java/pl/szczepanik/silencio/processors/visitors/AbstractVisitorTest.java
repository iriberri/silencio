package pl.szczepanik.silencio.processors.visitors;

import org.junit.Test;

import pl.szczepanik.silencio.GenericTest;
import pl.szczepanik.silencio.core.Key;
import pl.szczepanik.silencio.core.Value;
import pl.szczepanik.silencio.stubs.StubAbstractVisitor;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class AbstractVisitorTest extends GenericTest {

    @Test
    public void shouldFailWhenPassingValueToProcessValue() {

        // given
        Key key = new Key("123");
        Object value = new Value("321");
        AbstractVisitor visitor = new StubAbstractVisitor();

        // then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(AbstractVisitor.EXCEPTION_MESSAGE_INVALID_VALUE_TYPE);
        visitor.processValue(key, value);
    }
}
