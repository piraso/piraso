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

import ard.piraso.server.GroupChainId;
import ard.piraso.server.proxy.RegexMethodInterceptorAdapter;
import ard.piraso.server.proxy.RegexMethodInterceptorEvent;
import ard.piraso.server.proxy.RegexProxyFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * {@link DataSource} logger proxy factory.
 */
public class DataSourceProxyFactory extends AbstractSQLProxyFactory<DataSource> {

    public DataSourceProxyFactory(GroupChainId id) {
        super(id, new RegexProxyFactory<DataSource>(DataSource.class));

        factory.addMethodListener("getConnection", new GetConnectionListener());
    }

    private class GetConnectionListener extends RegexMethodInterceptorAdapter<DataSource> {
        @Override
        public void afterCall(RegexMethodInterceptorEvent<DataSource> evt) {
            // the current request retrieves a db connection
            // which means the current request is in logging scope.
            evaluator.requestOnScope();

            if(getPref().isConnectionEnabled()) {
                Connection connection = (Connection) evt.getReturnedValue();
                GroupChainId newId = id.create("connection-", connection.hashCode());

                evt.setReturnedValue(new ConnectionProxyFactory(newId).getProxy(connection));
            }
        }
    }
}
