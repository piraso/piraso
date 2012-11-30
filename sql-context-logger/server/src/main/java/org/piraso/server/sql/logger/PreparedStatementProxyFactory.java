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

package org.piraso.server.sql.logger;

import org.piraso.api.Level;
import org.piraso.api.entry.ElapseTimeEntry;
import org.piraso.api.sql.SQLPreferenceEnum;
import org.piraso.api.sql.SQLViewEntry;
import org.piraso.proxy.RegexMethodInterceptorAdapter;
import org.piraso.proxy.RegexMethodInterceptorEvent;
import org.piraso.proxy.RegexProxyFactory;
import org.piraso.server.GroupChainId;
import org.piraso.server.dispatcher.ContextLogDispatcher;
import org.piraso.server.logger.MessageLoggerListener;
import org.piraso.server.logger.MethodCallLoggerListener;
import org.apache.commons.lang.ArrayUtils;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * {@link PreparedStatement} proxy logger factory.
 */
public class PreparedStatementProxyFactory extends AbstractSQLProxyFactory<PreparedStatement> {

    private static final Level METHOD_CALL_LEVEL = Level.get(SQLPreferenceEnum.PREPARED_STATEMENT_METHOD_CALL_ENABLED.getPropertyName());

    private static final Level BASE_LEVEL = Level.get(SQLPreferenceEnum.PREPARED_STATEMENT_ENABLED.getPropertyName());

    private static final Level SQL_VIEW_LEVEL = Level.get(SQLPreferenceEnum.VIEW_SQL_ENABLED.getPropertyName());

    private String sql;

    private StatementParameterListener<PreparedStatement> parameterListener;

    public PreparedStatementProxyFactory(GroupChainId id, String sql) {
        super(id, new RegexProxyFactory<PreparedStatement>(PreparedStatement.class));

        this.sql = sql;

        if(getPref().isPreparedStatementMethodCallEnabled()) {
            factory.addMethodListener(".*", new MethodCallLoggerListener<PreparedStatement>(METHOD_CALL_LEVEL, id));
        }

        if(getPref().isViewSQLEnabled()) {
            parameterListener = new StatementParameterListener<PreparedStatement>();

            factory.addMethodListener("set.*", parameterListener);
        }

        factory.addMethodListener("executeBatch", new MessageLoggerListener<PreparedStatement> (BASE_LEVEL, id, "Execution Elapse Time"));
        factory.addMethodListener("executeQuery|executeUpdate|execute|addBatch", new ExecuteSQLListener());
    }

    private class ExecuteSQLListener extends RegexMethodInterceptorAdapter<PreparedStatement> {
        private ElapseTimeEntry elapseTime = new ElapseTimeEntry();

        @Override
        public void beforeCall(RegexMethodInterceptorEvent<PreparedStatement> evt) {
            elapseTime.start();
        }

        @Override
        public void afterCall(RegexMethodInterceptorEvent<PreparedStatement> evt) {
            Method method = evt.getInvocation().getMethod();

            // ensure to only listen to method with no arguments
            if(!ArrayUtils.isEmpty(method.getParameterTypes())) {
                return;
            }

            if(getPref().isViewSQLEnabled()) {
                elapseTime.stop();

                ContextLogDispatcher.forward(SQL_VIEW_LEVEL, id, new SQLViewEntry(sql, parameterListener.getParameters(), elapseTime));

                // to be reused
                parameterListener.clear();
            }

            if(getPref().isResultSetEnabled() && ResultSet.class.isAssignableFrom(method.getReturnType())) {
                ResultSet resultSet = (ResultSet) evt.getReturnedValue();
                GroupChainId newId = id.create("resultset-", resultSet.hashCode());

                evt.setReturnedValue(new ResultSetProxyFactory(newId).getProxy(resultSet));
            }
        }
    }
}
