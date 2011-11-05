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

package ard.piraso.api.entry;

/**
 * Defines an exception entry.
 */
public class ThrowableEntry extends Entry {
    private String message;

    private ThrowableEntry cause;

    private StackTraceElementEntry[] stackTrace;

    public ThrowableEntry() {}

    public ThrowableEntry(Throwable e) {
        message = e.getMessage();

        StackTraceElement[] elements = e.getStackTrace();
        stackTrace = new StackTraceElementEntry[elements.length];

        for(int i = 0; i < elements.length; i++) {
            stackTrace[i] = new StackTraceElementEntry(elements[i]);
        }

        if(e.getCause() != null) {
            cause = new ThrowableEntry(e.getCause());
        }
    }

    public StackTraceElementEntry[] getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTraceElementEntry[] stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ThrowableEntry getCause() {
        return cause;
    }

    public void setCause(ThrowableEntry cause) {
        this.cause = cause;
    }
}
