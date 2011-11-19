package ard.piraso.api.io;

import ard.piraso.api.JacksonUtils;
import ard.piraso.api.entry.Entry;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Base class for {@link PirasoEntryLoader}.
 */
public abstract class AbstractPirasoEntryLoader implements PirasoEntryLoader {

    protected ObjectMapper mapper;

    protected  AbstractPirasoEntryLoader() {
        mapper = JacksonUtils.createMapper();
    }

    public abstract Entry loadEntry(String className, String content) throws IOException, ClassNotFoundException;
}
