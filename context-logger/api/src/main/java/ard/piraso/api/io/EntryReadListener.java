package ard.piraso.api.io;

import java.util.EventListener;

/**
 * Defines an interface of entry read listener.
 */
public interface EntryReadListener extends EventListener {

    /**
     * Triggered when an entry was read.
     *
     * @param evt the entry read event object.
     */
    public void readEntry(EntryReadEvent evt);
}
