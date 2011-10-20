package ard.piraso.server.dispatcher;

import ard.piraso.api.entry.Entry;
import ard.piraso.server.logger.TraceableID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Singleton class for dispatching log entries to monitors.
 */
public class LogEntryDispatcher {

    private static final LogEntryDispatcher DISPATCHER = new LogEntryDispatcher();

    /**
     * Forward log entry for dispatch. Delegates to {@link #forwardEntry(TraceableID, Entry)}.
     *
     * @param id the entry traceable id
     * @param entry the log entry to be dispatched.
     */
    public static void forward(TraceableID id, Entry entry) {
        DISPATCHER.fireForwardedEvent(id, entry);
    }

    private List<DispatcherForwardListener> listeners = Collections.synchronizedList(new LinkedList<DispatcherForwardListener>());

    /**
     * Private constructor to not allow to instantiate this class.
     */
    private LogEntryDispatcher() {}

    /**
     * Forward log entry for dispatch.
     *
     * @param id the entry traceable id
     * @param entry the log entry to be dispatched.
     */
    public void forwardEntry(TraceableID id, Entry entry) {
        // todo implementation here

        fireForwardedEvent(id, entry);
    }

    public void fireForwardedEvent(TraceableID id, Entry entry) {
        DispatcherForwardEvent evt = new DispatcherForwardEvent(this, entry, id);

        List<DispatcherForwardListener> tmp = new ArrayList<DispatcherForwardListener>(listeners);
        for(DispatcherForwardListener listener : tmp) {
            listener.forwarded(evt);
        }
    }

    public void addListener(DispatcherForwardListener listener) {
        listeners.add(listener);
    }

    public void removeListener(DispatcherForwardListener listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }
}
