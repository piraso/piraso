package ard.piraso.server.service;

import ard.piraso.api.Preferences;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Registry of piraso users.
 */
public class UserRegistry {

    /**
     * lock instance
     */
    private final Lock lock = new ReentrantLock();

    /**
     * Contains the map of {@link User} and {@link ResponseLoggerService}.
     */
    private Map<User, ResponseLoggerService> userLoggerMap = Collections.synchronizedMap(new HashMap<User, ResponseLoggerService>(20));

    /**
     * Retrieve all {@link Preferences} given the monitored address.
     *
     * @param request the http servlet request
     * @return list of {@link Preferences}
     * @throws IOException on io error
     */
    public List<Preferences> getContextPreferences(HttpServletRequest request) throws IOException {
        List<Preferences> list = new LinkedList<Preferences>();

        List<ResponseLoggerService> tmp = new ArrayList<ResponseLoggerService>(userLoggerMap.values());
        for(ResponseLoggerService rl : tmp) {
            Preferences preferences = rl.getPreferences();
            if(rl.isAlive() && rl.getMonitoredAddr().equals(getMonitoredAddr(request)) &&
                    preferences.isUrlAcceptable(request.getRequestURI())) {
                list.add(preferences);
            }
        }

        return list;
    }

    /**
     * Retrieve all {@link ResponseLoggerService} given the monitored address.
     *
     * @param request the http servlet request
     * @return list of {@link ResponseLoggerService}
     * @throws IOException on io error
     */
    public List<ResponseLoggerService> getContextLoggers(HttpServletRequest request) throws IOException {
        List<ResponseLoggerService> list = new LinkedList<ResponseLoggerService>();

        List<ResponseLoggerService> tmp = new ArrayList<ResponseLoggerService>(userLoggerMap.values());
        for(ResponseLoggerService rl : tmp) {
            Preferences preferences = rl.getPreferences();
            if(rl.isAlive() && rl.getMonitoredAddr().equals(getMonitoredAddr(request)) &&
                    preferences.isUrlAcceptable(request.getRequestURI())) {
                list.add(rl);
            }
        }

        return list;
    }

    private String getMonitoredAddr(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    public boolean isUserExist(User user) {
        lock.lock();

        try {
            return userLoggerMap.containsKey(user);
        } finally {
            lock.unlock();
        }
    }

    public User createOrGetUser(HttpServletRequest request) {
        return new User(request);
    }

    public ResponseLoggerService getLogger(User user) {
        return userLoggerMap.get(user);
    }

    private ResponseLoggerService stopServiceIfExist(User user) throws IOException {
        if(isUserExist(user)) {
            ResponseLoggerService logger = userLoggerMap.get(user);
            logger.stop();

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
            stopServiceIfExist(user);
            userLoggerMap.put(user, service);
        } finally {
            lock.unlock();
        }
    }

    public void removeUser(User user) throws IOException {
        lock.lock();

        try {
            if(isUserExist(user)) {
                stopServiceIfExist(user);

                userLoggerMap.remove(user);
            }
        } finally {
            lock.unlock();
        }
    }
}
