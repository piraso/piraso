package org.piraso.server.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.piraso.api.PirasoLogger;
import org.piraso.api.Preferences;
import org.piraso.server.PirasoEntryPoint;
import org.piraso.server.PirasoRequest;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultUserRegistryImpl implements UserRegistry {

    private static final Log LOG = PirasoLogger.getUserRegistry();

    /**
     * lock instance
     */
    private final Lock lock = new ReentrantLock();

    /**
     * Contains the map of {@link User} and {@link ResponseLoggerService}.
     */
    private Map<User, ResponseLoggerService> userLoggerMap = Collections.synchronizedMap(new LinkedHashMap<User, ResponseLoggerService>(20));

    /**
     * Retrieve all {@link org.piraso.api.Preferences} given the monitored address.
     *
     * @param entryPoint the http servlet request
     * @return list of {@link org.piraso.api.Preferences}
     * @throws java.io.IOException on io error
     */
    public List<Preferences> getContextPreferences(PirasoEntryPoint entryPoint) throws IOException {
        List<Preferences> list = new LinkedList<Preferences>();

        List<ResponseLoggerService> tmp = new ArrayList<ResponseLoggerService>(userLoggerMap.values());
        for(ResponseLoggerService rl : tmp) {
            Preferences preferences = rl.getPreferences();
            if(rl.isAlive() && rl.isWatched(getMonitoredAddr(entryPoint)) &&
                    preferences.isUrlAcceptable(entryPoint.getPath())) {
                list.add(preferences);
            }
        }

        return list;
    }

    public Map<User, ResponseLoggerService> getUserLoggerMap() {
        return new HashMap<User, ResponseLoggerService>(userLoggerMap);
    }

    /**
     * Retrieve all {@link ResponseLoggerService} given the monitored address.
     *
     * @param request the http servlet request
     * @return list of {@link ResponseLoggerService}
     * @throws IOException on io error
     */
    public List<ResponseLoggerService> getContextLoggers(PirasoEntryPoint request) throws IOException {
        List<ResponseLoggerService> list = new LinkedList<ResponseLoggerService>();

        List<ResponseLoggerService> tmp = new ArrayList<ResponseLoggerService>(userLoggerMap.values());
        for(ResponseLoggerService rl : tmp) {
            Preferences preferences = rl.getPreferences();
            if(rl.isAlive() && rl.isWatched(getMonitoredAddr(request)) &&
                    preferences.isUrlAcceptable(request.getPath())) {
                list.add(rl);
            }
        }

        return list;
    }

    public void clear() {
        userLoggerMap.clear();
    }

    private String getMonitoredAddr(PirasoEntryPoint request) {
        return request.getRemoteAddr();
    }

    public boolean isWatched(PirasoEntryPoint request) throws IOException {
        return CollectionUtils.isNotEmpty(getContextLoggers(request));
    }

    public boolean isUserExist(User user) {
        lock.lock();

        try {
            return userLoggerMap.containsKey(user);
        } finally {
            lock.unlock();
        }
    }

    public User createOrGetUser(PirasoRequest request) {
        return new User(request);
    }

    public ResponseLoggerService getLogger(User user) {
        return userLoggerMap.get(user);
    }

    private ResponseLoggerService stopServiceIfExist(User user) throws IOException {
        if(isUserExist(user)) {
            ResponseLoggerService logger = userLoggerMap.get(user);

            if(logger.isAlive()) {
                logger.stop();
            }

            return logger;
        }

        return null;
    }

    /**
     * Associate user with a {@link ResponseLoggerService}.
     *
     * @param user the user
     * @param service the response logger service
     * @throws java.io.IOException on io error
     */
    public void associate(User user, ResponseLoggerService service) throws IOException {
        lock.lock();

        try {
            if(LOG.isInfoEnabled() && user != null) {
                LOG.info(String.format(
                        "[PIRASO USER]: User ASSOCIATED with address '%s' and activity id '%s'.",
                        user.getRemoteAddr(),
                        user.getActivityUuid())
                );
            }

            stopServiceIfExist(user);
            service.addStopListener(new StoppedLoggerHandler());

            userLoggerMap.put(user, service);
        } finally {
            lock.unlock();
        }
    }

    public void removeUser(User user) throws IOException {
        lock.lock();

        try {
            if(isUserExist(user)) {
                if(LOG.isInfoEnabled() && user != null) {
                    LOG.info(String.format(
                            "[PIRASO USER]: User REMOVED with address '%s' and activity id '%s'.",
                            user.getRemoteAddr(),
                            user.getActivityUuid())
                    );
                }

                stopServiceIfExist(user);

                userLoggerMap.remove(user);
            }
        } finally {
            lock.unlock();
        }
    }

    private class StoppedLoggerHandler implements StopLoggerListener {

        public void stopped(StopLoggerEvent evt) {
            lock.lock();

            try {
                ResponseLoggerService service = (ResponseLoggerService) evt.getSource();
                removeUser(service.getUser());
            } catch (IOException e) {
                LOG.warn(e.getMessage(), e);
            } finally {
                lock.unlock();
            }
        }
    }
}
