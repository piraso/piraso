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

package org.piraso.api.entry;

import java.io.PrintStream;

/**
 * Defines an exception entry.
 */
public class ThrowableEntry extends Entry {
    private String message;

    private String actualToString;

    private ThrowableEntry cause;

    private StackTraceElementEntry[] stackTrace;

    public ThrowableEntry() {}

    public ThrowableEntry(Throwable e) {
        actualToString = e.toString();
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

    public String getActualToString() {
        return actualToString;
    }

    public void setActualToString(String actualToString) {
        this.actualToString = actualToString;
    }

    /**
     * Prints this throwable and its backtrace to the specified print stream.
     *
     * @param s <code>PrintStream</code> to use for output
     */
    public void printStackTrace(PrintStream s) {
        synchronized (s) {
            s.println(actualToString);

            StackTraceElementEntry[] trace = getStackTrace();
            for (StackTraceElementEntry aTrace : trace) {
                s.println("\tat " + aTrace);
            }

            ThrowableEntry ourCause = getCause();
            if (ourCause != null)
                ourCause.printStackTraceAsCause(s, trace);
        }
    }

    /**
     * Print our stack trace as a cause for the specified stack trace.
     *
     * @param s           the print stream
     * @param causedTrace the base stack trace
     */
    private void printStackTraceAsCause(PrintStream s,
                                        StackTraceElementEntry[] causedTrace) {
        StackTraceElementEntry[] trace = getStackTrace();
        int m = trace.length - 1, n = causedTrace.length - 1;

        while (m >= 0 && n >= 0 && trace[m].equals(causedTrace[n])) {
            m--;
            n--;
        }

        int framesInCommon = trace.length - 1 - m;

        s.println("Caused by: " + actualToString);
        for (int i = 0; i <= m; i++) {
            s.println("\tat " + trace[i]);
        }

        if (framesInCommon != 0) {
            s.println("\t... " + framesInCommon + " more");
        }

        ThrowableEntry ourCause = getCause();
        if (ourCause != null) {
            ourCause.printStackTraceAsCause(s, trace);
        }
    }

    @Override
    public String toString() {
        String s = getClass().getName();
        String message = getMessage();
        return (message != null) ? (s + ": " + message) : s;
    }
}
