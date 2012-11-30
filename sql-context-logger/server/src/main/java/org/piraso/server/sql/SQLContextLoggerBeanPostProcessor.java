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

package org.piraso.server.sql;

import org.piraso.server.AbstractContextLoggerBeanProcessor;

import javax.sql.DataSource;

/**
 * Create a bean post processor which ensures that any bean instance of type {@link DataSource} will
 * be wrap by a context logger aware instance.
 *
 */
public class SQLContextLoggerBeanPostProcessor extends AbstractContextLoggerBeanProcessor<DataSource> {

    public SQLContextLoggerBeanPostProcessor() {
        super(DataSource.class);
    }

    @Override
    public DataSource createProxy(DataSource o, String id) {
        return SQLContextLogger.create(o, id);
    }
}
