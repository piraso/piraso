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

package ard.piraso.io;

import ard.piraso.api.io.EntryReadEvent;
import ard.piraso.io.util.IOEntryRequest;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adleon
 */
public class IOEntryManager {
    private static final Logger LOG = Logger.getLogger(IOEntryManager.class.getName());
    
    private String id;

    private Map<Long, IOEntryRequest> requests;
    
    public IOEntryManager(String id) {
        this.id = id;
        this.requests = Collections.synchronizedMap(new LinkedHashMap<Long, IOEntryRequest>());
        
        LOG.log(Level.INFO, "IOManager created for {0}", id);
    }

    public Collection<IOEntryRequest> getRequests() {
        return requests.values();
    }
    
    private IOEntryRequest createOrGetRequest(Long requestId) {
        IOEntryRequest request = requests.get(requestId);
        
        if(request == null) {
            request = new IOEntryRequest(id, requestId);
            requests.put(requestId, request);
        }
        
        return request;
    }
    
    public synchronized IOEntry addEntry(EntryReadEvent evt) throws IOException {
        IOEntryRequest request = createOrGetRequest(evt.getRequestId());
        
        return request.addEntry(evt);
    }

    public String getId() {
        return id;
    }

    public void visit(IOEntryVisitor visitor) throws IOException {
        List<IOEntryRequest> tmp = new ArrayList<IOEntryRequest>(requests.values());

        for(IOEntryRequest request : tmp) {
            request.visit(visitor);
        }
    }
    
    public void visit(List<Long> requestIds, IOEntryVisitor visitor) throws IOException {
        List<IOEntryRequest> tmp = new ArrayList<IOEntryRequest>(requests.values());
        
        for(IOEntryRequest request : tmp) {
            if(!requestIds.contains(request.getId())) {
                continue;
            }
            
            request.visit(visitor);
        }
    }

    public IOEntry getEntryAt(Long requestId, int rowNum) throws IOException {
        if(!requests.containsKey(requestId)) {
            throw new IllegalArgumentException(String.format("Request with id '%d' not found.", requestId));
        }

        return createOrGetRequest(requestId).get(rowNum);
    }
    
    public int size(Long requestId) {
        if(!requests.containsKey(requestId)) {
            throw new IllegalArgumentException(String.format("Request with id '%d' not found.", requestId));
        }
        
        return createOrGetRequest(requestId).size();
    }

    public IOEntry getRequest(Long requestId) {
        if(!requests.containsKey(requestId)) {
            throw new IllegalArgumentException(String.format("Request with id '%d' not found.", requestId));
        }

        return createOrGetRequest(requestId).getRequest();
    }
}
