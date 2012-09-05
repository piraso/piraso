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

import java.util.EventObject;

/**
 *
 * @author adleon
 */
public class IOEntryEvent extends EventObject {
    
    private IOEntry entry;
    
    private String id;
    
    private String watchedAddr;
    
    public IOEntryEvent(Object source, String id, String watchedAddr) {
        super(source);
        
        this.id = id;
        this.watchedAddr = watchedAddr;
    }

    public IOEntryEvent(Object source, IOEntry entry) {
        super(source);
        
        this.entry = entry;
    }

    public IOEntry getEntry() {
        return entry;
    }

    public String getId() {
        return id;
    }

    public String getWatchedAddr() {
        return watchedAddr;
    }
}
