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

package org.piraso.api.io;

import org.piraso.api.entry.Entry;

import java.util.Date;
import java.util.EventObject;

/**
 * Defines a read entry event.
 */
public class EntryReadEvent<T> extends EventObject {

    private Entry entry;

    private Long requestId;

    private Date date;

    private String watchedAddr;

    private String id;

    public EntryReadEvent(Object o, String id, String watchedAddr) {
        super(o);
        this.id = id;
        this.watchedAddr = watchedAddr;
    }

    public EntryReadEvent(Object o, Long requestId, Entry entry, Date date) {
        super(o);

        this.requestId = requestId;
        this.entry = entry;
        this.date = date;
    }

    public String getWatchedAddr() {
        return watchedAddr;
    }

    public String getId() {
        return id;
    }

    public Entry getEntry() {
        return entry;
    }

    public Long getRequestId() {
        return requestId;
    }

    public Date getDate() {
        return date;
    }
}
