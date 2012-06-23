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

package ard.piraso.server.sql;

import ard.piraso.server.GroupChainId;
import ard.piraso.server.sql.logger.DataSourceProxyFactory;

import javax.sql.DataSource;

/**
 * Utility class for wrapping {@link DataSource} object with the {@link DataSource} proxy logger.
 */
public final class SQLContextLogger {

    /**
     * Wraps the given {@link DataSource} to add support for context logging.
     *
     * @param dataSource {@link DataSource} to add context logging support.
     * @param id the unique identification for this dataSource.
     * @return the proxy {@link DataSource} with context logging support.
     */
    public static DataSource create(DataSource dataSource, String id) {
        DataSourceProxyFactory factory = new DataSourceProxyFactory(new GroupChainId(id));

        return factory.getProxy(dataSource);
    }

}
