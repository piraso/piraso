/*
 * Copyright (c) 2011. Piraso Alvin R. de Leon. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The Piraso licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ard.piraso.server.service;

import ard.piraso.api.PirasoLogger;
import ard.piraso.api.Preferences;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Registry of piraso users.
 */
public class UserRegistry {

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
            if(rl.isAlive() && rl.getWatchedAddr().equals(getMonitoredAddr(request)) &&
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
            if(rl.isAlive() && rl.getWatchedAddr().equals(getMonitoredAddr(request)) &&
                    preferences.isUrlAcceptable(request.getRequestURI())) {
                list.add(rl);
            }
        }

        return list;
    }

    private String getMonitoredAddr(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    public boolean isWatched(HttpServletRequest request) throws IOException {
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
            if(LOG.isInfoEnabled() && user != null) {
                LOG.info(String.format(
                        "[PIRASO USER]: User ASSOCIATED with address '%s' and activity id '%s'.",
                        user.getRemoteAddr(),
                        user.getActivityUuid())
                );
            }

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
}
