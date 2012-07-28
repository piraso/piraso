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

import ard.piraso.api.converter.ObjectConverterRegistry;

/**
 * Contains helper method for {@link ObjectEntry} class.
 *
 */
public final class ObjectEntryUtils {

    private ObjectEntryUtils() {}

    public static String toString(ObjectEntry entry) {
        if(entry == null || entry.isNull()) {
            return "@null";
        }

        if(!entry.isSupported()) {
            return "@not-supported";
        }

        if(entry.isSupported()) {
            return String.valueOf(entry.toObject());
        }

        return entry.getStrValue();
    }

    public static Object toObject(ObjectEntry entry) {
        if(entry == null || entry.isNull()) {
            return null;
        }

        if(!entry.isSupported()) {
            throw new IllegalStateException(String.format("Unsupported with class %s", entry.getClassName()));
        }

        return ObjectConverterRegistry.convertToObject(entry.getClassName(), entry.getStrValue());
    }
}
