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

import ard.piraso.api.entry.Entry;
import ard.piraso.io.util.IOEntrySerializable;

import java.io.IOException;
import java.util.Date;

/**
 *
 * @author adleon
 */
public class IOEntry {
    
    private Long rowNum;
    
    private Long id;
    
    private Date date;
    
    private Entry entry;
    
    public IOEntry(IOEntrySerializable wrapper) throws IOException {
        this(wrapper, null);
    }
    
    public IOEntry(IOEntrySerializable wrapper, Entry entry) throws IOException {
        this.rowNum = wrapper.getRowNum();
        this.id = wrapper.getId();
        this.date = wrapper.getDate();
        
        if(entry == null) {
            this.entry = wrapper.toEntry();
        } else {
            this.entry = entry;
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRowNum() {
        return rowNum;
    }

    public void setRowNum(Long rowNum) {
        this.rowNum = rowNum;
    }
}
