package ard.piraso.server.service;

import ard.piraso.api.Preferences;
import ard.piraso.api.entry.Entry;

import java.io.IOException;

/**
 * Defines an interface for a response logger service.
 */
public interface ResponseLoggerService {

    public User getUser();

    public String getId();

    public String getWatchedAddr();

    public Preferences getPreferences();

    public void start() throws Exception;

    public void stop() throws IOException;

    public boolean isAlive();

    public boolean isForcedStopped();

    public void stopAndWait(long timeout) throws InterruptedException, IOException;

    public void log(Entry entry) throws IOException;
}
