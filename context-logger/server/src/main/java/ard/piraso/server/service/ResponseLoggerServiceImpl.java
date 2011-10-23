package ard.piraso.server.service;

import ard.piraso.api.IDGenerator;
import ard.piraso.api.Preferences;
import ard.piraso.api.entry.Entry;
import ard.piraso.server.logger.TraceableID;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Response logger service implementation.
 */
public class ResponseLoggerServiceImpl implements ResponseLoggerService {

    private static final int MAX_QUEUE_FORCE_KILL_SIZE = 1000;

    private static final IDGenerator ID_GENERATOR = new IDGenerator();

    private User user;

    private UserRegistry registry;

    private long requestId;

    private ResponseLoggerServiceImpl(UserRegistry registry, HttpServletRequest originalRequest) throws IOException {
        this.registry = registry;
        requestId = ID_GENERATOR.next();
    }

    public User getUser() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getActivityUuid() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getMonitoredAddr() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void start() throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void stop() throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isAlive() throws IOException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isStopped() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void waitTillStopped(long timeout) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Preferences getPreferences() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void log(TraceableID id, Entry entry) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
