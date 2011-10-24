package ard.piraso.server.service;

import java.io.IOException;

/**
 * Thrown when the service was forced stopped.
 */
public class ForcedStoppedException extends IOException {

    /**
     * Construct given the reason why service was forced stopped..
     *
     * @param s  the reason why service was forced stopped.
     */
    public ForcedStoppedException(String s) {
        super(s);
    }
}
