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

import org.piraso.api.JacksonUtils;
import org.piraso.api.entry.Entry;
import org.piraso.api.io.EntryReadEvent;
import org.piraso.api.io.PirasoObjectLoaderRegistry;

import java.io.IOException;
import java.util.Date;

/**
 *
 * @author adleon
 */
public class IOEntrySerializable {
   
    private String classType;
        
    private Long id;
    
    private Date date;
    
    private String entryValue;

    private Long rowNum;

    public IOEntrySerializable() {
    }

    public IOEntrySerializable(EntryReadEvent evt) throws IOException {
        this.id = evt.getRequestId();
        this.date = evt.getDate();
        this.classType = evt.getEntry().getClass().getName();
        
        this.entryValue = JacksonUtils.MAPPER.writeValueAsString(evt.getEntry());
    }

    public Long getRowNum() {
        return rowNum;
    }

    public void setRowNum(Long rowNum) {
        this.rowNum = rowNum;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEntryValue() {
        return entryValue;
    }

    public void setEntryValue(String entryValue) {
        this.entryValue = entryValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Entry toEntry() throws IOException {
        return (Entry) PirasoObjectLoaderRegistry.INSTANCE.loadObject(classType, entryValue);
    }
}
