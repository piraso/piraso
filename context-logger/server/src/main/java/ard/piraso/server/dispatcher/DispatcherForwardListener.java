package ard.piraso.server.dispatcher;

import java.util.EventListener;

/**
 * Defines an interface for listening to forwarded log entries.
 */
public interface DispatcherForwardListener extends EventListener {

    /**
     * Triggered when a log entry is forwarded for dispatch.
     *
     * @param evt the dispatch forward event
     */
    public void forwarded(DispatcherForwardEvent evt);
}
