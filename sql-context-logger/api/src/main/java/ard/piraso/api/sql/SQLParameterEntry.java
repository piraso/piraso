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

package ard.piraso.api.sql;

import ard.piraso.api.entry.EntryUtils;
import ard.piraso.api.entry.MethodCallEntry;
import ard.piraso.api.entry.ObjectEntry;

import java.lang.reflect.Method;


/**
 * SQL parameter entry
 */
public class SQLParameterEntry extends MethodCallEntry {

    private String name;

    private Integer index;

    public SQLParameterEntry() {}

    public SQLParameterEntry(String name, Method method, Object[] arguments) {
        this(name, method, arguments, null);
    }

    public SQLParameterEntry(String name, Method method, Object[] arguments, Object returnValue) {
        super(method);
        this.name = name;
        setArguments(EntryUtils.toEntry(arguments));
        setReturnedValue(new ObjectEntry(returnValue));
    }

    public SQLParameterEntry(Integer index, Method method, Object[] arguments) {
        this(index, method, arguments, null);
    }

    public SQLParameterEntry(Integer index, Method method, Object[] arguments, Object returnValue) {
        super(method);
        this.index = index;
        setArguments(EntryUtils.toEntry(arguments));
        setReturnedValue(new ObjectEntry(returnValue));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
