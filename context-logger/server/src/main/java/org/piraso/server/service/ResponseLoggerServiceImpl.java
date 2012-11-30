/*
 * Copyright (c) 2012. Piraso Alvin R. de Leon. All Rights Reserved.
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

package org.piraso.server.service;

import org.piraso.api.GeneralPreferenceEnum;
import org.piraso.api.IDGenerator;
import org.piraso.api.JacksonUtils;
import org.piraso.api.Preferences;
import org.piraso.api.entry.Entry;
import org.piraso.api.io.PirasoEntryWriter;
import org.piraso.server.IOUtils;
import org.piraso.server.PirasoRequest;
import org.piraso.server.PirasoResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;

import javax.swing.event.EventListenerList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Response logger service implementation.
 */
public class ResponseLoggerServiceImpl implements ResponseLoggerService {

    /**
     * final class logging instance.
     */
    private static final Log LOG = LogFactory.getLog(ResponseLoggerServiceImpl.class);

    /**
     * If queue size reaches this size, the service will auto stop.
     */
    private static final int DEFAULT_MAX_QUEUE_FORCE_KILL_SIZE = 2000;

    /**
     * Maximum of 30 minutes idle time, otherwise the service will auto stop.
     */
    private static final long DEFAULT_MAX_IDLE_TIME_OUT = 60 * 60 * 1000;

    /**
     * The response content type.
     */
    private static final String RESPONSE_CONTENT_TYPE = "text/xml; charset=UTF-8";

    private static final IDGenerator ID_GENERATOR = new IDGenerator();


    /**
     * The transfer queue. This holds the queue which will be streamed to response writer.
     */
    private List<Entry> transferQueue = Collections.synchronizedList(new LinkedList<Entry>());

    /**
     * The user which monitors.
     */
    private User user;

    /**
     * The user watched remote address
     */
    private String watchedAddr;

    /**
     * Some alternative watched address given the watched address
     */
    private String[] alternativeWatchedAddrs;

    /**
     * The user watched remote address regular expression pattern
     */
    private Pattern watchedAddrPattern;

    /**
     * The response transfer.
     */
    private PirasoResponse response;

    /**
     * Responsible for writing transfer entries to response stream writer.
     */
    private PirasoEntryWriter writer;

    /**
     * Determines whether the service is still active or not.
     */
    private boolean alive = true;

    /**
     * The user logging preference.
     */
    private Preferences preferences;

    /**
     * Determines the current idle time. If this exceed the {@link #maxIdleTimeout} the service will be forced
     * stopped.
     */
    private long currentIdleTime = 0l;

    /**
     * Reason why service was forced stopped.
     */
    private String forcedStoppedReason;

    /**
     * Determines whether the service was force stopped.
     */
    private boolean forcedStopped = false;

    /**
     * maximum idle timeout
     */
    private long maxIdleTimeout = DEFAULT_MAX_IDLE_TIME_OUT;

    /**
     * maximum transfer queue force stopped size
     */
    private int maxQueueForceKillSize = DEFAULT_MAX_QUEUE_FORCE_KILL_SIZE;

    private long globalId;

    private ObjectMapper mapper;

    private EventListenerList listeners = new EventListenerList();


    /**
     * Construct the service given the user, request and response.
     *
     * @param user the monitor {@link User}.
     * @param request the current request object.
     * @param response the current response object.
     * @throws IOException on io error
     */
    public ResponseLoggerServiceImpl(User user, PirasoRequest request, PirasoResponse response) throws IOException {
        this.preferences = request.getPreferences();
        this.watchedAddr = request.getWatchedAddr();
        this.watchedAddrPattern = Pattern.compile(request.getWatchedAddr());
        this.alternativeWatchedAddrs = AlternativeWatchedAddressProviderManager.INSTANCE.getAlternatives(watchedAddr);
        this.user = user;
        this.response = response;
        this.mapper = JacksonUtils.createMapper();
        this.globalId = ID_GENERATOR.next();
    }

    /**
     * Sets the maximum idle timeout. When timeout is reached this service will be force stopped. This is measured in milliseconds.
     *
     * @param maxIdleTimeout the maximum idle timeout
     */
    public void setMaxIdleTimeout(long maxIdleTimeout) {
        this.maxIdleTimeout = maxIdleTimeout;
    }

    /**
     * Sets the maximum transfer queue size before the service will be forced stopped.
     *
     * @param maxQueueForceKillSize the maximum transfer queue size
     */
    public void setMaxQueueForceKillSize(int maxQueueForceKillSize) {
        this.maxQueueForceKillSize = maxQueueForceKillSize;
    }

    /**
     * {@inheritDoc}
     */
    public User getUser() {
        return user;
    }

    /**
     * {@inheritDoc}
     */
    public String getWatchedAddr() {
        return watchedAddr;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isWatched(String remoteAddr) {
        if(watchedAddr.equals(remoteAddr)) {
            return true;
        }

        if(watchedAddrPattern.matcher(remoteAddr).matches()) {
            return true;
        }

        if(!ArrayUtils.isEmpty(alternativeWatchedAddrs)) {
            for(String addr : alternativeWatchedAddrs) {
                if(addr.equals(remoteAddr)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    public String getId() {
        return getUser().getActivityUuid();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isForcedStopped() {
        return forcedStopped;
    }

    /**
     * {@inheritDoc}
     */
    public void start() throws IOException, TransformerConfigurationException, ParserConfigurationException {
        response.setContentType(RESPONSE_CONTENT_TYPE);
        response.setCharacterEncoding("UTF-8");

        try {
            writer = new PirasoEntryWriter(getId(), getWatchedAddr(), response.getWriter());

            doLogWhileAlive();
        } finally {
            synchronized (this) {
                IOUtils.closeQuitely(writer);
                notifyAll();
            }
        }
    }

    /**
     * Wait for {@code 1800000l} millis if not interrupted for a log. This will ensure that the owning thread will idle
     * when the {@link #transferQueue} is empty.
     * <p>
     * This is also responsible for computing for the idle time, which when the idle time exceeds the limit
     * {@link #DEFAULT_MAX_IDLE_TIME_OUT} the service will forced stopped.
     *
     * @throws IOException on io error
     */
    private void waitTillNoEntry() throws IOException {
        if(transferQueue.isEmpty()) {
            long start = System.currentTimeMillis();

            try {
                long timeout = 1800000l;

                if(timeout >= maxIdleTimeout) {
                    timeout = maxIdleTimeout;
                }

                wait(timeout);
            } catch (InterruptedException ignored) {}

            // compute for idle time
            currentIdleTime += System.currentTimeMillis() - start;

            if(currentIdleTime >= maxIdleTimeout) {
                forcedStopped = true;
                forcedStoppedReason = String.format("Idle timeout '%d' was reached.", maxIdleTimeout);
            }
        }
    }

    /**
     * Throws a {@link ForcedStoppedException} when forced stopped.
     *
     * @throws ForcedStoppedException on forced stopped.
     */
    private void throwWhenForcedStopped() throws ForcedStoppedException {
        if(forcedStopped) {
            alive = false;
            throw new ForcedStoppedException(forcedStoppedReason);
        }
    }

    /**
     * Empty the transfer queue and write to response stream writer. This will also reset the {@link #currentIdleTime}
     * to {@code 0}.
     */
    private void writeAllTransfer() {
        while(CollectionUtils.isNotEmpty(transferQueue) && !isForcedStopped()) {
            try {
                Entry entry = transferQueue.remove(0);

                // only do this for no id request
                if(preferences.isEnabled(GeneralPreferenceEnum.NO_REQUEST_CONTEXT.getPropertyName())) {
                    StringWriter writer = new StringWriter();
                    mapper.writeValue(writer, entry);
                    entry = mapper.readValue(writer.toString(), entry.getClass());

                    entry.setRequestId(globalId);
                }

                writer.write(entry);
                currentIdleTime = 0;
            } catch (Exception e) {
                LOG.warn(e.getMessage(), e);
            }
        }
    }

    /**
     * Ensure to wait and do log while still not stopped or was forced to stop.
     *
     * @throws IOException on io error
     */
    private void doLogWhileAlive() throws IOException {
        synchronized (this) {
            try {
                while(isAlive()) {
                    waitTillNoEntry();
                    writeAllTransfer();
                    throwWhenForcedStopped();
                }
            } finally {
                fireStopEvent(new StopLoggerEvent(this));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void stop() throws IOException {
        synchronized(this) {
            alive = false;
            notifyAll();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void stopAndWait(long timeout) throws IOException, InterruptedException {
        synchronized(this) {
            if(isAlive()) {
                stop();
                wait(timeout);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public Preferences getPreferences() {
        return preferences;
    }

    /**
     * {@inheritDoc}
     */
    public void log(Entry entry) throws IOException {
        synchronized (this) {
            Validate.notNull(entry.getRequestId(), "Entry id should not be null.");

            if(transferQueue.size() + 1 >= maxQueueForceKillSize) {
                forcedStopped = true;
                forcedStoppedReason = String.format("Max queue force kill size '%d' was reached.", maxQueueForceKillSize);
                notifyAll();

                return;
            }

            transferQueue.add(entry);
            notifyAll();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addStopListener(StopLoggerListener listener) {
        listeners.add(StopLoggerListener.class, listener);
    }

    /**
     * {@inheritDoc}
     */
    public void removeStopListener(StopLoggerListener listener) {
        listeners.remove(StopLoggerListener.class, listener);
    }

    /**
     * {@inheritDoc}
     */
    public void fireStopEvent(StopLoggerEvent event) {
        for(StopLoggerListener listener : Arrays.asList(listeners.getListeners(StopLoggerListener.class))) {
            listener.stopped(event);
        }
    }
}
