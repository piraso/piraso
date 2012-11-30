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

package org.piraso.io;

import org.piraso.api.io.EntryReadEvent;
import org.piraso.api.io.EntryReadListener;
import org.apache.commons.lang.Validate;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adleon
 */
public class IOEntryReader implements EntryReadListener {
    
    private static final Logger LOG = Logger.getLogger(IOEntryReader.class.getName());

    private IOEntryManager manager;

    private IOEntrySource source;

    private IOEntryEvent startEvent;

    private EventListenerList listeners = new EventListenerList();

    public IOEntryReader(IOEntrySource source) {
        Validate.notNull(source, "Source should not be null.");
        
        this.source = source;
    }

    public IOEntryReader createNew() {
        source.removeListener(this);

        return new IOEntryReader(source.createNew());
    }
    
    public String getId() {
        return source.getId();
    }
    
    public String getWatchedAddr() {
        return source.getWatchedAddr();
    }
    
    public void start() {
        if(!source.isAlive()) {
            source.reset();
        }

        try {
            if(!source.isAlive()) {
                source.addListener(this);
                source.start();
            }
        } finally {
            fireStoppedEvent(startEvent);
        }
    }

    public boolean isRestartable() {
        return source.isRestartable();
    }
    
    public void stop() {
        source.stop();
    }
    
    public boolean isAlive() {
        return source.isAlive();
    }

    public IOEntryManager getManager() {
        return manager;
    }
    
    @Override
    public synchronized void readEntry(EntryReadEvent evt) {
        if(manager == null) {
            manager = new IOEntryManager(source.getId());
        }
        
        try {
            IOEntry entry = manager.addEntry(evt);
            
            fireEntryReadEvent(new IOEntryEvent(this, entry));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public void started(EntryReadEvent evt) {
        startEvent = new IOEntryEvent(this, evt.getId(), evt.getWatchedAddr());
        fireStartedEvent(startEvent);
    }
    
    public void fireStartedEvent(IOEntryEvent evt) {
        for(IOEntryLifecycleListener listener : Arrays.asList(listeners.getListeners(IOEntryLifecycleListener.class))) {
            listener.started(evt);
        }
        for(IOEntryLifecycleListener listener : Arrays.asList(listeners.getListeners(IOEntryListener.class))) {
            listener.started(evt);
        }
    }

    public void fireStoppedEvent(IOEntryEvent evt) {
        for(IOEntryLifecycleListener listener : Arrays.asList(listeners.getListeners(IOEntryLifecycleListener.class))) {
            listener.stopped(evt);
        }

        for(IOEntryLifecycleListener listener : Arrays.asList(listeners.getListeners(IOEntryListener.class))) {
            listener.stopped(evt);
        }
    }

    public void fireEntryReadEvent(IOEntryEvent evt) {
        for(IOEntryReceivedListener listener : Arrays.asList(listeners.getListeners(IOEntryReceivedListener.class))) {
            listener.receivedEntry(evt);
        }

        for(IOEntryReceivedListener listener : Arrays.asList(listeners.getListeners(IOEntryListener.class))) {
            listener.receivedEntry(evt);
        }
    }

    public void addListener(IOEntryListener listener) {
        listeners.add(IOEntryListener.class, listener);
    }

    public void removeListener(IOEntryListener listener) {
        listeners.remove(IOEntryListener.class, listener);
    }

    public void addLiveCycleListener(IOEntryLifecycleListener listener) {
        listeners.add(IOEntryLifecycleListener.class, listener);
    }

    public void removeLiveCycleListener(IOEntryLifecycleListener listener) {
        listeners.remove(IOEntryLifecycleListener.class, listener);
    }

    public void addReceivedListener(IOEntryReceivedListener listener) {
        listeners.add(IOEntryReceivedListener.class, listener);
    }
}
