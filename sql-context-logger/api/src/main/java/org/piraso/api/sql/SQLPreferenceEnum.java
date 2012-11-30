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

package org.piraso.api.sql;

import org.piraso.api.Level;
import org.piraso.api.PreferenceEnum;

/**
 * SQL preferences enum
 */
public enum SQLPreferenceEnum  implements PreferenceEnum {
    CONNECTION_ENABLED("sql.connection.enabled"),

    CONNECTION_METHOD_CALL_ENABLED("sql.connection.method.call.enabled"),

    PREPARED_STATEMENT_ENABLED("sql.prepared.statement.enabled"),

    VIEW_SQL_ENABLED("sql.view.enabled"),

    PREPARED_STATEMENT_METHOD_CALL_ENABLED("sql.prepared.statement.method.call.enabled"),

    RESULTSET_ENABLED("sql.resultset.enabled"),

    RESULTSET_METHOD_CALL_ENABLED("sql.resultset.method.call.enabled"),

    RESULTSET_DATA_ENABLED("sql.resultset.data.enabled"),

    RESULTSET_ALL_DATA_ENABLED("sql.resultset.all.data.enabled"),

    VIEW_DATA_SIZE("sql.data.size", false);

    // register enum as level
    static {
        Level.addLevels(values());
    }

    private String propertyName;

    private boolean level;

    private SQLPreferenceEnum(String propertyName) {
        this(propertyName, true);
    }

    private SQLPreferenceEnum(String propertyName, boolean level) {
        this.propertyName = propertyName;
        this.level = level;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public boolean isLevel() {
        return level;
    }
}
