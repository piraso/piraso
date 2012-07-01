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

package ard.piraso.server;

import ard.piraso.api.*;
import ard.piraso.api.entry.*;
import ard.piraso.server.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.*;

/**
 * Represents the current request context.
 */
public class PirasoContext implements ContextPreference {

    /**
     * final class logging instance.
     */
    private static final Log LOG = LogFactory.getLog(PirasoContext.class);

    private static final Log LOG_ENTRY_POINT = PirasoLogger.getEntryPoint();

    private static final IDGenerator ID_GENERATOR = new IDGenerator();

    private UserRegistry registry;

    private PirasoEntryPoint entryPoint;

    private long requestId;

    private ReferenceRequestEntry ref;
    
    private Map<Class<?>, Map<String, Object>> propertyBag = new HashMap<Class<?>, Map<String, Object>>();

    private LinkedList<EntryHolder> scopedEntryQueue = new LinkedList<EntryHolder>();

    private List<ResponseLoggerService> requestScoped = Collections.synchronizedList(new LinkedList<ResponseLoggerService>());

    private GroupChainId refGroupChainId;

    public PirasoContext(PirasoEntryPoint entryPoint) {
        this(entryPoint, UserRegistrySingleton.INSTANCE.getRegistry());
    }

    public PirasoContext(PirasoEntryPoint entryPoint, UserRegistry registry) {
        this(entryPoint, registry, null);
    }

    public PirasoContext(PirasoEntryPoint entryPoint, UserRegistry registry, ReferenceRequestEntry ref) {
        this(ID_GENERATOR.next(), entryPoint, registry, ref, null);

        if(LOG_ENTRY_POINT.isDebugEnabled()) {
            LOG_ENTRY_POINT.debug(String.format(
                    "[PIRASO ENTRY POINT]: Request[thread=%s, hash=%s, id=%d, addr=%s] '%s' is being watched.",
                    Thread.currentThread().getId() + ":" + Thread.currentThread().getName(),
                    Integer.toHexString(entryPoint.hashCode()),
                    requestId,
                    entryPoint.getRemoteAddr(),
                    entryPoint.getPath())
            );
        }
    }

    private PirasoContext(long requestId, PirasoEntryPoint entryPoint, UserRegistry registry, ReferenceRequestEntry ref, GroupChainId refGroupChainId) {
        this.requestId = requestId;
        this.registry = registry;
        this.entryPoint = entryPoint;
        this.ref = ref;
        this.refGroupChainId = refGroupChainId;
    }

    public PirasoContext createChildContext(GroupChainId chainId) {
        PirasoContext context = new PirasoContext(requestId, entryPoint, registry, ref, chainId);

        context.scopedEntryQueue = scopedEntryQueue;
        context.requestScoped = requestScoped;
        context.propertyBag = propertyBag;

        return context;
    }

    /**
     * {@inheritDoc}
     */
    public void addProperty(Class<?> clazz, String name, Object value) {
        Map<String, Object> map = propertyBag.get(clazz);
        if(map == null) {
            map = new HashMap<String, Object>();
            propertyBag.put(clazz, map);
        }
        
        map.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    public Object getProperty(Class<?> clazz, String name) {
        Map<String, Object> map = propertyBag.get(clazz);

        if(map == null) {
            return null;
        }

        return map.get(name);
    }

    /**
     * {@inheritDoc}
     */
    public Long getRequestId() {
        return requestId;
    }

    /**
     * {@inheritDoc}
     */
    public ReferenceRequestEntry getRef() {
        return ref;
    }

    /**
     * {@inheritDoc}
     */
    public PirasoEntryPoint getEntryPoint() {
        return entryPoint;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isMonitored() {
        try {
            return CollectionUtils.isNotEmpty(registry.getContextPreferences(entryPoint));
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isRegexEnabled(String property) {
        try {
            List<Preferences> preferencesList = registry.getContextPreferences(entryPoint);

            for(Preferences pref : preferencesList) {
                if(pref.isRegexEnabled(property)) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEnabled(String property) {
        try {
            List<Preferences> preferencesList = registry.getContextPreferences(entryPoint);

            for(Preferences pref : preferencesList) {
                if(pref.isEnabled(property)) {
                    return true;
                }
            }

            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Integer getIntValue(String property) {
        try {
            List<Preferences> preferencesList = registry.getContextPreferences(entryPoint);
            Integer max = null;

            for(Preferences pref : preferencesList) {
                Integer intValue = pref.getIntValue(property);
                if(max == null || (intValue != null && intValue > max)) {
                    max = intValue;
                }
            }

            return max;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Log the given entry.
     *
     * @param level preference property
     * @param id the entry id
     * @param entry the entry to log
     */
    public void log(Level level, GroupChainId id, Entry entry) {
        // always store scoped entries
        if(Level.SCOPED.equals(level)) {
            scopedEntryQueue.add(new EntryHolder(id, entry));
        }

        if(!isMonitored()) {
            // ignore any logs when current context is not monitored.
            return;
        }

        if(level == null) {
            level = Level.ALL;
        }

        try {
            List<ResponseLoggerService> loggers = registry.getContextLoggers(entryPoint);

            if(Level.SCOPED.equals(level)) {
                for(ResponseLoggerService logger : loggers) {
                    Preferences preferences = logger.getPreferences();

                    synchronized (this) {
                        if(requestScoped.contains(logger) || !preferences.isEnabled(GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName())) {
                            doLog(logger, Level.SCOPED, id, entry);

                            if(!requestScoped.contains(logger)) {
                                logger.addStopListener(new StoppedLoggerHandler(logger));
                                requestScoped.add(logger);
                            }
                        }
                    }
                }

                return;
            }

            for(ResponseLoggerService logger : loggers) {
                Preferences preferences = logger.getPreferences();

                if(Level.ALL.equals(level)) {
                    doLog(logger, level, id, entry);
                } else if(RegexLevelEntryAware.class.isInstance(entry)) {
                    if(preferences.isRegexEnabled(level.getName())) {
                        doLog(logger, level, id, entry);
                    }
                } else if(preferences.isEnabled(level.getName())) {
                    doLog(logger, level, id, entry);
                }
            }
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    /**
     * Ensure to populate the group and request id.
     *
     * @param logger the logger service
     * @param level the log level
     * @param id the chain group
     * @param entry the log entry
     * @throws IOException on error
     */
    private void doLog(ResponseLoggerService logger, Level level, GroupChainId id, Entry entry) throws IOException {
        synchronized (this) {
            if(!requestScoped.contains(logger) && Level.SCOPED != level) {
                logger.addStopListener(new StoppedLoggerHandler(logger));
                requestScoped.add(logger);

                for(EntryHolder holder : scopedEntryQueue) {
                    doLog(logger, Level.SCOPED, holder.id, holder.entry);
                }
            }
        }

        if(refGroupChainId != null) {
            entry.setReferenceGroup(new GroupEntry(refGroupChainId.getGroupIds()));
        }

        entry.setRequestId(requestId);
        entry.setBaseRequestId(requestId);
        entry.setLevel(level.getName());
        entry.setGroup(new GroupEntry(id.getGroupIds()));

        if(ref != null && RequestEntry.class.isInstance(entry)) {
            ((RequestEntry) entry).setReference(ref);
        }

        logger.log(entry);
    }

    private class EntryHolder {
        private GroupChainId id;

        private Entry entry;

        private EntryHolder(GroupChainId id, Entry entry) {
            this.id = id;
            this.entry = entry;
        }
    }

    private class StoppedLoggerHandler implements StopLoggerListener {

        private ResponseLoggerService logger;

        private StoppedLoggerHandler(ResponseLoggerService logger) {
            this.logger = logger;
        }

        public void stopped(StopLoggerEvent evt) {
            requestScoped.remove(logger);
            logger.removeStopListener(this);
        }
    }
}
