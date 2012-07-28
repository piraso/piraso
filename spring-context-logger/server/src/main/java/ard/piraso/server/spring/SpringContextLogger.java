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

package ard.piraso.server.spring;

import ard.piraso.server.GroupChainId;
import ard.piraso.server.spring.logger.SpringRemotingProxyFactory;
import ard.piraso.server.spring.remoting.HttpInvokerRequestExecutorWrapper;
import org.springframework.remoting.httpinvoker.HttpInvokerRequestExecutor;

/**
 * Create proxy spring context loggers
 */
public final class SpringContextLogger {
    
    public static HttpInvokerRequestExecutor createHttpInvoker(HttpInvokerRequestExecutor executor, String id) {
        HttpInvokerRequestExecutorWrapper wrapper = new HttpInvokerRequestExecutorWrapper(executor);
        SpringRemotingProxyFactory factory = new SpringRemotingProxyFactory(new GroupChainId(id));

        return factory.getProxy(wrapper);
    }
}
