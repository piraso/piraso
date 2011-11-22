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
 * Unless required by applicable law or agreed
 * to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ard.piraso.server.log4j.logger;

import ard.piraso.proxy.RegexMethodInterceptorAdapter;
import ard.piraso.proxy.RegexMethodInterceptorEvent;
import ard.piraso.proxy.RegexProxyFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;

/**
 * Proxy factory for {@link LoggerRepository}.
 */
public class LoggerRepositoryProxyFactory extends AbstractLog4jProxyFactory<LoggerRepository> {
    public LoggerRepositoryProxyFactory() {
        super(new RegexProxyFactory<LoggerRepository>(LoggerRepository.class));

        factory.addMethodListener("getLogger|getRootLogger|exists", new RegexMethodInterceptorAdapter<LoggerRepository>() {
            @Override
            public void afterCall(RegexMethodInterceptorEvent<LoggerRepository> evt) {
                String category = null;
                if(evt.getInvocation().getMethod().getParameterTypes().length > 0) {
                    category = String.valueOf(evt.getInvocation().getArguments()[0]);
                }

                // ensure that there is no conflict on springframework aop classes
                if(category != null && !category.startsWith("org.springframework.aop")) {
                    Logger logger = (Logger) evt.getReturnedValue();

                    if(Log4JLogger.class.isInstance(logger)) {
                        return;
                    }

                    Logger returnedValue = Log4JLoggerEnhancer.wrap(logger);
                    evt.setReturnedValue(new LoggerProxyFactory(category).getProxy(returnedValue));
                }
            }
        });
    }

}