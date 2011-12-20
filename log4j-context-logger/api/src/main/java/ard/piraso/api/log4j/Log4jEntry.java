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

package ard.piraso.api.log4j;

import ard.piraso.api.entry.*;

/**
 * Log4J log entry.
 */
public class Log4jEntry extends Entry implements StackTraceAwareEntry, ThrowableAwareEntry, RegexLevelEntryAware {

    private String logLevel;

    private String message;

    private StackTraceElementEntry[] stackTrace;

    private ThrowableEntry throwable;

    public Log4jEntry() {}

    public Log4jEntry(String logLevel, String message) {
        this(logLevel, message, null);
    }

    public Log4jEntry(String logLevel, String message, ThrowableEntry throwable) {
        this.logLevel = logLevel;
        this.message = message;
        this.throwable = throwable;
    }

    public ThrowableEntry getThrown() {
        return throwable;
    }

    public void setThrown(ThrowableEntry throwable) {
        this.throwable = throwable;
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

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
}
