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

package ard.piraso.server.sql.logger;

import ard.piraso.api.Level;
import ard.piraso.api.sql.SQLPreferenceEnum;
import ard.piraso.proxy.RegexMethodInterceptorAdapter;
import ard.piraso.proxy.RegexMethodInterceptorEvent;
import ard.piraso.proxy.RegexProxyFactory;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.logger.MethodCallLoggerListener;
import ard.piraso.server.logger.SimpleMethodLoggerListener;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * SQL connection factory, this will create the proxy instance responsible for logging sql connection specific entries.
 */
public class ConnectionProxyFactory extends AbstractSQLProxyFactory<Connection> {

    private static final Level METHOD_CALL_LEVEL = Level.get(SQLPreferenceEnum.CONNECTION_METHOD_CALL_ENABLED.getPropertyName());

    private static final Level BASE_LEVEL = Level.get(SQLPreferenceEnum.CONNECTION_ENABLED.getPropertyName());

    public ConnectionProxyFactory(GroupChainId id) {
        super(id, new RegexProxyFactory<Connection>(Connection.class));

        if(getPref().isConnectionMethodCallEnabled()) {
            factory.addMethodListener(".*", new MethodCallLoggerListener<Connection>(METHOD_CALL_LEVEL, id));
        }

        if(getPref().isConnectionEnabled()) {
            factory.addMethodListener("close|commit|rollback", new SimpleMethodLoggerListener<Connection>(BASE_LEVEL, id));

            if(getPref().isPreparedStatementEnabled()) {
                factory.addMethodListener("prepareStatement", new PreparedStatementListener());
            }
        }
    }

    private class PreparedStatementListener extends RegexMethodInterceptorAdapter<Connection> {
        @Override
        public void afterCall(RegexMethodInterceptorEvent<Connection> evt) {
            PreparedStatement statement = (PreparedStatement) evt.getReturnedValue();
            GroupChainId newId = id.create("statement-", statement.hashCode());

            newId.addProperty(Connection.class, wrappedObject);
            String sql = (String) evt.getInvocation().getArguments()[0];

            evt.setReturnedValue(new PreparedStatementProxyFactory(newId, sql).getProxy(statement));
        }
    }
}
