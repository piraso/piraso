package ard.piraso.api.io;

import ard.piraso.api.entry.Entry;

import java.io.IOException;

/**
 * Base Loader
 */
public class BasePirasoEntryLoader extends AbstractPirasoEntryLoader {

    @Override
    @SuppressWarnings("unchecked")
    public Entry loadEntry(String className, String content) throws IOException, ClassNotFoundException {
        Class clazz = Class.forName(className);
        return (Entry) mapper.readValue(content, clazz);
    }
}
