package ard.piraso.server.service;

import java.util.EventListener;

/**
 * Defines an interface of stop logger listeners.
 */
public interface StopLoggerListener extends EventListener {

    /**
     * Event triggered on {@link ResponseLoggerService} stopped.
     *
     * @param evt the event object
     */
    void stopped(StopLoggerEvent evt);
}
