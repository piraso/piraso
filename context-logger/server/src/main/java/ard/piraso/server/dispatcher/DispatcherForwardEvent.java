package ard.piraso.server.dispatcher;

import ard.piraso.api.entry.Entry;
import ard.piraso.server.logger.TraceableID;

import java.util.EventObject;

/**
 * Dispatcher forward event object.
 */
public class DispatcherForwardEvent extends EventObject {

    private Entry entry;

    private TraceableID id;

    public DispatcherForwardEvent(Object source, Entry entry, TraceableID id) {
        super(source);

        this.entry = entry;
        this.id = id;
    }

    public Entry getEntry() {
        return entry;
    }

    public TraceableID getId() {
        return id;
    }
}
