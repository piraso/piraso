package ard.piraso.server.service;

import ard.piraso.api.Preferences;
import ard.piraso.api.entry.Entry;
import ard.piraso.server.logger.TraceableID;

import java.io.IOException;

/**
 * Defines an interface for a response logger service.
 */
public interface ResponseLoggerService {

    public User getUser();

    public long getId();

    public String getMonitoredAddr();

    public void start() throws Exception;

    public void stop() throws IOException;

    public boolean isAlive() throws IOException;

    public void stopAndWait(long timeout) throws InterruptedException;

    public Preferences getPreferences();

    public void log(TraceableID id, Entry entry) throws IOException;
}
