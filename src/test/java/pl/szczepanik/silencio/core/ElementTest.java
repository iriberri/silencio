package pl.szczepanik.silencio.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Damian Szczepanik <damianszczepanik@github>
 */
public class ElementTest {

    @Test
    public void shouldReturnValue() {

        // given
        String value = "twoValue";

        // when
        Value element = new Value(value);

        // then
        assertThat(element.getValue()).isEqualTo(value);
    }

}
