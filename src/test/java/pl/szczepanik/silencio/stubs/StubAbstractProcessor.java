package pl.szczepanik.silencio.stubs;

import java.io.Reader;
import java.io.Writer;

import pl.szczepanik.silencio.api.Format;
import pl.szczepanik.silencio.api.Strategy;
import pl.szczepanik.silencio.core.AbstractProcessor;

/**
 * Stub of @link {@link AbstractProcessor} that has only stub methods.
 * 
 * @author Damian Szczepanik <damianszczepanik@github>
 */
public class StubAbstractProcessor extends AbstractProcessor {

    public StubAbstractProcessor(Format format, Strategy[] strategies) {
        super(format, strategies);
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