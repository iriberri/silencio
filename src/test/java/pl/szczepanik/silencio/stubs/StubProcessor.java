package pl.szczepanik.silencio.stubs;

import java.io.Reader;
import java.io.Writer;

import pl.szczepanik.silencio.api.Converter;
import pl.szczepanik.silencio.api.Format;
import pl.szczepanik.silencio.core.AbstractProcessor;

/**
 * Stub of @link {@link AbstractProcessor} that has only stub methods.
 * 
 * @author Damian Szczepanik <damianszczepanik@github>
 */
public class StubProcessor extends AbstractProcessor {

    public StubProcessor(Format format, Converter[] converters) {
        this(format);
        setConverters(converters);
    }

    public StubProcessor(Format format) {
        super(format);
    }

    @Override
    public void realLoad(Reader reader) {
    }

    @Override
    public void realProcess() {

    }

    @Override
    public void realWrite(Writer writer) {
    }

}