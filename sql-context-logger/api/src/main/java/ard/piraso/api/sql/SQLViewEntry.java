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

import ard.piraso.api.entry.ElapseTimeAware;
import ard.piraso.api.entry.ElapseTimeEntry;
import ard.piraso.api.entry.Entry;

import java.util.HashMap;
import java.util.Map;

/**
 * SQL View entry
 */
public class SQLViewEntry extends Entry implements ElapseTimeAware {

    private ElapseTimeEntry elapseTime;

    private Map<Integer, SQLParameterEntry> parameters;

    private String sql;

    public SQLViewEntry() {}

    public SQLViewEntry(String sql, Map<Integer, SQLParameterEntry> parameters, ElapseTimeEntry elapseTime) {
        this.sql = sql;
        this.parameters = new HashMap<Integer, SQLParameterEntry>(parameters);
        this.elapseTime = elapseTime;
    }

    public ElapseTimeEntry getElapseTime() {
        return elapseTime;
    }

    public void setElapseTime(ElapseTimeEntry elapseTime) {
        this.elapseTime = elapseTime;
    }

    public Map<Integer, SQLParameterEntry> getParameters() {
        return parameters;
    }

    public void setParameters(Map<Integer, SQLParameterEntry> parameters) {
        this.parameters = parameters;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
