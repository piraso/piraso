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
import org.piraso.api.entry.MessageEntry;
import org.piraso.api.sql.SQLPreferenceEnum;
import org.piraso.proxy.RegexMethodInterceptorAdapter;
import org.piraso.proxy.RegexMethodInterceptorEvent;
import org.piraso.proxy.RegexProxyFactory;
import org.piraso.server.GroupChainId;
import org.piraso.server.dispatcher.ContextLogDispatcher;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * {@link DataSource} logger proxy factory.
 */
public class DataSourceProxyFactory extends AbstractSQLProxyFactory<DataSource> {

    private static final Level BASE_LEVEL = Level.get(SQLPreferenceEnum.CONNECTION_ENABLED.getPropertyName());

    @SuppressWarnings("unchecked")
    public DataSourceProxyFactory(Class sourceClass, GroupChainId id) {
        super(id, new RegexProxyFactory(sourceClass));

        factory.addMethodListener("getConnection", new GetConnectionListener());
    }

    private class GetConnectionListener extends RegexMethodInterceptorAdapter<DataSource> {
        @Override
        public void afterCall(RegexMethodInterceptorEvent<DataSource> evt) {
            if(getPref().isConnectionEnabled()) {
                Connection connection = (Connection) evt.getReturnedValue();
                GroupChainId newId = id.create("connection-", connection.hashCode());

                MessageEntry entry = new MessageEntry("open");
                ContextLogDispatcher.forward(BASE_LEVEL, newId, entry);

                evt.setReturnedValue(new ConnectionProxyFactory(newId).getProxy(connection));
            }
        }
    }
}
