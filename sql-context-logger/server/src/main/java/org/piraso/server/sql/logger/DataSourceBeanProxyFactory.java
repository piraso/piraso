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

import org.piraso.proxy.RegexMethodInterceptorAdapter;
import org.piraso.proxy.RegexMethodInterceptorEvent;
import org.piraso.proxy.RegexProxyFactory;
import org.piraso.server.GroupChainId;
import org.springframework.beans.factory.FactoryBean;

import javax.sql.DataSource;

/**
 * {@link javax.sql.DataSource} logger proxy factory.
 */
public class DataSourceBeanProxyFactory extends AbstractSQLProxyFactory<FactoryBean> {

    @SuppressWarnings("unchecked")
    public DataSourceBeanProxyFactory(Class sourceClass, GroupChainId id) {
        super(id, new RegexProxyFactory(sourceClass));

        factory.addMethodListener("getObject", new GetObjectListener());
    }

    private class GetObjectListener extends RegexMethodInterceptorAdapter<FactoryBean> {
        @Override
        public void afterCall(RegexMethodInterceptorEvent<FactoryBean> evt) {
            DataSource dataSource = (DataSource) evt.getReturnedValue();
            evt.setReturnedValue(new DataSourceProxyFactory(DataSource.class, id).getProxy(dataSource));
        }
    }
}
