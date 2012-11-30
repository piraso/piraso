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

package org.piraso.server.log4j.logger;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;

/**
 * Wraps a {@link Logger} with {@link Log4JLogger} class.
 */
public class Log4JLoggerEnhancer {

    public static Log4JLogger wrap(Logger logger) {
        return (Log4JLogger) Enhancer.create(Log4JLogger.class, new LoggerMethodInterceptor(logger));
    }

    private static class LoggerMethodInterceptor implements MethodInterceptor {

        private Logger logger;

        private LoggerMethodInterceptor(Logger logger) {
            this.logger = logger;
        }

        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            return method.invoke(logger, objects);
        }
    }
}
