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

package org.piraso.server.java;

import org.piraso.server.AbstractContextLoggerBeanProcessor;

import java.util.concurrent.ExecutorService;

/**
 * {@link java.util.concurrent.ExecutorService} bean post processor.
 */
public class ExecutorContextLoggerBeanPostProcessor extends AbstractContextLoggerBeanProcessor<ExecutorService> {
    public ExecutorContextLoggerBeanPostProcessor() {
        super(ExecutorService.class);
    }

    @Override
    public ExecutorService createProxy(ExecutorService o, String id) {
        return ExecutorContextLogger.create(o, id);
    }
}
