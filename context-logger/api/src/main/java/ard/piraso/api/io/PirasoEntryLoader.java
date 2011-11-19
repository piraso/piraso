package ard.piraso.api.io;

import ard.piraso.api.entry.Entry;

import java.io.IOException;

/**
 * Loads an entry class.
 */
public interface PirasoEntryLoader {

    public Entry loadEntry(String className, String content) throws IOException, ClassNotFoundException;
}
