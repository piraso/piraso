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

package ard.piraso.server.sql;

import ard.piraso.api.sql.SQLPreferenceEnum;
import ard.piraso.server.GeneralPreferenceEvaluator;

/**
 * Evaluates SQL preferences.
 */
public class SQLPreferenceEvaluator extends GeneralPreferenceEvaluator {

    private static final int DEFAULT_RETURN_SIZE = 100;


    public boolean isConnectionMethodCallEnabled() {
        return isEnabled(SQLPreferenceEnum.CONNECTION_METHOD_CALL_ENABLED);
    }

    public boolean isConnectionEnabled() {
        return isEnabled(SQLPreferenceEnum.CONNECTION_ENABLED);
    }

    public boolean isPreparedStatementMethodCallEnabled() {
        return isEnabled(SQLPreferenceEnum.PREPARED_STATEMENT_METHOD_CALL_ENABLED);
    }

    public boolean isPreparedStatementEnabled() {
        return isEnabled(SQLPreferenceEnum.PREPARED_STATEMENT_ENABLED);
    }

    public boolean isViewSQLEnabled() {
        return isEnabled(SQLPreferenceEnum.VIEW_SQL_ENABLED);
    }

    public boolean isResultSetEnabled() {
        return isEnabled(SQLPreferenceEnum.RESULTSET_ENABLED);
    }

    public boolean isResultSetDataEnabled() {
        return isEnabled(SQLPreferenceEnum.RESULTSET_DATA_ENABLED);
    }

    public boolean isResultSetAllDataEnabled() {
        return isEnabled(SQLPreferenceEnum.RESULTSET_ALL_DATA_ENABLED);
    }

    public boolean isResultSetMethodCallEnabled() {
        return isEnabled(SQLPreferenceEnum.RESULTSET_METHOD_CALL_ENABLED);
    }

    public int getMaxDataSize() {
        Integer value = getIntValue(SQLPreferenceEnum.VIEW_DATA_SIZE);

        return value == null ? DEFAULT_RETURN_SIZE : value;
    }
}
