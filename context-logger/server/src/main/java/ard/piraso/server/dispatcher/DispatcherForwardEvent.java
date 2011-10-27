package ard.piraso.server.dispatcher;

import ard.piraso.api.entry.Entry;
import ard.piraso.server.GroupChainId;

import java.util.EventObject;

/**
 * Dispatcher forward event object.
 */
public class DispatcherForwardEvent extends EventObject {

    private Entry entry;

    private GroupChainId id;

    public DispatcherForwardEvent(Object source, Entry entry, GroupChainId id) {
        super(source);

        this.entry = entry;
        this.id = id;
    }

    public Entry getEntry() {
        return entry;
    }

    public GroupChainId getId() {
        return id;
    }
}
