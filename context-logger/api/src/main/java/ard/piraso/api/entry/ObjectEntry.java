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

import ard.piraso.api.converter.ObjectConverterRegistry;

/**
 * Object entry type
 */
public class ObjectEntry extends Entry {

    private String strValue;

    private String className;

    private boolean supported;

    public ObjectEntry() {}

    public ObjectEntry(Object obj) {
        if(obj != null) {
            supported = ObjectConverterRegistry.isSupported(obj);
            className = obj.getClass().getName();

            if(supported) {
                strValue = ObjectConverterRegistry.convertToString(obj);
            }
        }
    }

    public boolean isNull() {
        return className == null;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isSupported() {
        return supported;
    }

    public void setSupported(boolean supported) {
        this.supported = supported;
    }

    /**
     * Converts this entry to actual object instance represented.
     *
     * @return the actual object represented
     */
    public Object toObject() {
        return ObjectEntryUtils.toObject(this);
    }
}
