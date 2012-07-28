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

/**
 * Contains helper method for processing entries.
 */
public final class EntryUtils {

    private EntryUtils() {}

    public static ObjectEntry[] toEntry(Object[] objs) {
        if(objs == null) {
            return null;
        }

        ObjectEntry[] entries = new ObjectEntry[objs.length];

        for(int i = 0; i < objs.length; i++) {
            entries[i] = new ObjectEntry(objs[i]);
        }

        return entries;
    }

    public static StackTraceElementEntry[] toEntry(StackTraceElement[] elements) {
        if(elements == null) {
            return null;
        }

        StackTraceElementEntry[] stackTrace = new StackTraceElementEntry[elements.length];

        for(int i = 0; i < elements.length; i++) {
            stackTrace[i] = new StackTraceElementEntry(elements[i]);
        }

        return stackTrace;
    }
}
