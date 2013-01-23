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

package org.piraso.io.util;

import org.piraso.api.LongIDGenerator;
import org.piraso.api.entry.RequestEntry;
import org.piraso.api.entry.ResponseEntry;
import org.piraso.api.io.EntryReadEvent;
import org.piraso.io.IOEntry;
import org.piraso.io.IOEntryVisitor;
import org.piraso.io.cache.IOEntryRequestCache;
import org.apache.commons.lang.Validate;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a single request of {@link IOEntry} received from a source.
 *
 * @author alvinrdeleon
 */
public class IOEntryRequest {
    
    private static final Logger LOG = Logger.getLogger(IOEntryRequest.class.getName());
    
    private Long id;

    private LongIDGenerator generator;
        
    private int count;

    private IOEntry request;
    
    private IOEntry response;

    private IOEntryRequestCache cache;

    private String parentId;
    
    public IOEntryRequest(String parentId, Long id) {
        this.cache = new IOEntryRequestCache(parentId, id);
        this.id = id;
        this.parentId = parentId;
        this.generator = new LongIDGenerator();

        LOG.log(Level.INFO, String.format("IO Request with id '%s'.", id));
    }
    
    public void visit(IOEntryVisitor visitor) throws IOException {
        for(int i = 0; i < count; i++) {
            visitor.visit(get(i));
        }
    }
    
    public synchronized IOEntry addEntry(EntryReadEvent evt) throws IOException {
        IOEntrySerializable serializable = new IOEntrySerializable(evt);

        LOG.log(Level.INFO, String.format("Request[%s]: Entry received of type '%s'.", id, evt.getEntry().getClass().getName()));
        
        serializable.setRowNum(generator.next() - 1);
        
        IOEntry ioEntry = new IOEntry(serializable, evt.getEntry());
        
        if(RequestEntry.class.isInstance(evt.getEntry()) && request == null) {
            request = ioEntry;
        } else if(ResponseEntry.class.isInstance(evt.getEntry()) && response == null) {
            response = ioEntry;

            LOG.log(Level.INFO, String.format("Request[%s]: Response received completed.", id));
        }
        
        cache.put(serializable);
        count++;
        
        return ioEntry;
    }

    public IOEntry get(int rowNum) throws IOException {
        IOEntrySerializable serializable = cache.get(rowNum);

        Validate.notNull(serializable, String.format("Request[%s, %d, %d] not found.", parentId, id, rowNum));

        return new IOEntry(serializable);
    }

    public Long getId() {
        return id;
    }

    public IOEntry getRequest() {
        return request;
    }

    public IOEntry getResponse() {
        return response;
    }
    
    public int size() {
        return count;
    }
}
