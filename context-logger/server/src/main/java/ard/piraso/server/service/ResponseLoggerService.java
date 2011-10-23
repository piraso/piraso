package ard.piraso.server.service;

import ard.piraso.api.Preferences;
import ard.piraso.api.entry.Entry;
import ard.piraso.server.logger.TraceableID;

import java.io.IOException;

/**
 * Represents a user service.
 */
public interface ResponseLoggerService {

    public User getUser();

    public String getActivityUuid();

    public String getMonitoredAddr();

    public void start() throws IOException;

    public void stop() throws IOException;

    public boolean isAlive() throws IOException;

    public boolean isStopped();
    
    public void waitTillStopped(long timeout);

    public Preferences getPreferences();

    public void log(TraceableID id, Entry entry);
}
