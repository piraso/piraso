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

package ard.piraso.server;

import ard.piraso.api.*;
import ard.piraso.api.entry.Entry;
import ard.piraso.api.entry.GroupEntry;
import ard.piraso.api.entry.ReferenceRequestEntry;
import ard.piraso.api.entry.RequestEntry;
import ard.piraso.server.service.ResponseLoggerService;
import ard.piraso.server.service.UserRegistry;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    private boolean requestOnScope;

    private long requestId;

    private ReferenceRequestEntry ref;
    
    private Map<Class<?>, Map<String, Object>> propertyBag = new HashMap<Class<?>, Map<String, Object>>();

    private LinkedList<EntryHolder> entryQueue = new LinkedList<EntryHolder>();

    public PirasoContext(PirasoEntryPoint entryPoint, UserRegistry registry) {
        this(entryPoint, registry, null);
    }

    public PirasoContext(PirasoEntryPoint entryPoint, UserRegistry registry, ReferenceRequestEntry ref) {
        this.requestId = ID_GENERATOR.next();
        this.registry = registry;
        this.entryPoint = entryPoint;
        this.ref = ref;

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
     * {@inheritDoc}
     */
    public void requestOnScope() {
        if(requestOnScope) {
            return;
        }

        requestOnScope = true;

        // ensure to log queued
        while(!entryQueue.isEmpty()) {
            EntryHolder holder = entryQueue.remove(0);
            logScoped(holder.id, holder.entry, true);
        }
    }

    private void logScoped(GroupChainId id, Entry entry, boolean scopedEnabled) {
        try {
            List<ResponseLoggerService> loggers = registry.getContextLoggers(entryPoint);

            for(ResponseLoggerService logger : loggers) {
                Preferences preferences = logger.getPreferences();

                boolean actual = preferences.isEnabled(GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName());
                if(actual == scopedEnabled) {
                    doLog(logger, Level.SCOPED, id, entry);
                }
            }
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
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
        if(!isMonitored()) {
            // ignore any logs when current context is not monitored.
            return;
        }

        if(level == null) {
            level = Level.ALL;
        }

        // only log for those that ignores logging scope
        if(Level.SCOPED.equals(level) && !requestOnScope) {
            logScoped(id, entry, false);

            if(isEnabled(Level.SCOPED.getName())) {
                entryQueue.add(new EntryHolder(id, entry));
            }

            return;
        } else if(!requestOnScope && !Level.SCOPED.equals(level)) {
            // when any other log is provided that is not scope aware then enable request for scope logging
            requestOnScope();
        }

        try {
            List<ResponseLoggerService> loggers = registry.getContextLoggers(entryPoint);

            for(ResponseLoggerService logger : loggers) {
                Preferences preferences = logger.getPreferences();

                if(Level.ALL.equals(level)) {
                    doLog(logger, level, id, entry);
                } else if(preferences.isEnabled(level.getName())) {
                    doLog(logger, level, id, entry);
                } else if(Level.SCOPED.equals(level) && requestOnScope) {
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
        entry.setRequestId(requestId);
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
}
