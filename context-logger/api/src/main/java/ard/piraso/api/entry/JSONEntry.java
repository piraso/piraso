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

package ard.piraso.api.entry;

import ard.piraso.api.JacksonUtils;

import java.io.IOException;

/**
 * JSON entry
 */
public class JSONEntry extends Entry implements MessageAwareEntry, ThrowableAwareEntry, StackTraceAwareEntry {

    private ThrowableEntry thrown;

    private String message;

    private String jsonString;

    private StackTraceElementEntry[] stackTrace;

    public JSONEntry() {
    }

    public JSONEntry(String message, Object jsonObj) throws IOException {
        setMessage(message);
        json(jsonObj);
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public void setThrown(ThrowableEntry thrown) {
        this.thrown = thrown;
    }

    public ThrowableEntry getThrown() {
        return thrown;
    }

    public void json(Object obj) throws IOException {
        setJsonString(JacksonUtils.MAPPER.writeValueAsString(obj));
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public StackTraceElementEntry[] getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTraceElementEntry[] stackTrace) {
        this.stackTrace = stackTrace;
    }
}
