package ard.piraso.server.service;

import java.util.EventObject;

/**
 * Stop logger event object.
 */
public class StopLoggerEvent extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public StopLoggerEvent(Object source) {
        super(source);
    }
}
