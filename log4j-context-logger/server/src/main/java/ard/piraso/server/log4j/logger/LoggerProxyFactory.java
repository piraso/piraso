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

package ard.piraso.server.log4j.logger;

import ard.piraso.api.entry.EntryUtils;
import ard.piraso.api.entry.ThrowableEntry;
import ard.piraso.api.log4j.Log4jEntry;
import ard.piraso.proxy.RegexMethodInterceptorAdapter;
import ard.piraso.proxy.RegexMethodInterceptorEvent;
import ard.piraso.proxy.RegexProxyFactory;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.dispatcher.ContextLogDispatcher;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import java.lang.reflect.Method;

/**
 * ProxyFactory for {@link org.apache.log4j.Logger} class.
 */
public class LoggerProxyFactory  extends AbstractLog4jProxyFactory<Logger> {

    private String category;

    public LoggerProxyFactory(String category) {
        super(new RegexProxyFactory<Logger>(Logger.class));
        this.category = category;
        this.id = new GroupChainId(category);

        factory.addMethodListener("debug|error|fatal|info|warn|trace|log", new LevelLogInterceptorListener());
        factory.addMethodListener("is*Enabled*", new LevelIsEnabledListener());
    }

    private class LevelIsEnabledListener extends RegexMethodInterceptorAdapter<Logger> {
        @Override
        public void afterCall(RegexMethodInterceptorEvent<Logger> evt) {
            if(!evt.getInvocation().getMethod().getReturnType().equals(Boolean.TYPE)) {
                return;
            }
            
            Boolean enabled = (Boolean) evt.getReturnedValue();
            if(enabled) {
                return;
            }

            if(getPref().isLog4jEnabled()) {
                Method method = evt.getInvocation().getMethod();
                if(method.getName().equals("isTraceEnabled")) {
                    evt.setReturnedValue(getPref().isLog4jEnabled(category, "TRACE"));
                } else if(method.getName().equals("isDebugEnabled")) {
                    evt.setReturnedValue(getPref().isLog4jEnabled(category, "DEBUG"));
                } else if(method.getName().equals("isInfoEnabled")) {
                    evt.setReturnedValue(getPref().isLog4jEnabled(category, "INFO"));
                } else if(method.getName().equals("isEnabledFor")) {
                    Priority priority = getArgument(evt, Priority.class);
                    evt.setReturnedValue(getPref().isLog4jEnabled(category, priority.toString()));
                }
            }
        }
    }

    private class LevelLogInterceptorListener extends RegexMethodInterceptorAdapter<Logger> {
        @Override
        public void beforeCall(RegexMethodInterceptorEvent<Logger> evt) {
            String level = evt.getInvocation().getMethod().getName().toUpperCase();

            if("LOG".equals(level)) {
                Priority priority = getArgument(evt, Priority.class);
                if(priority == null) {
                    return;
                }

                Object msg = getArgument(evt, Object.class);
                Throwable throwable = getArgument(evt, Throwable.class);

                logEntry(priority.toString(), msg, throwable);
                return;
            }

            if(!"LOG".equals(level)) {  // either debug|error|fatal|info|warn|trace
                Object msg = getArgument(evt, Object.class);
                Throwable throwable = getArgument(evt, Throwable.class);

                logEntry(level, msg, throwable);
            }
        }

        private void logEntry(String level, Object msg, Throwable throwable) {
            if(getPref().isLog4jEnabled(category, level)) {
                // we are on scope so start monitoring
                if(getPref().isLog4jEnabled()) {
                    getPref().requestOnScope();
                }

                Log4jEntry entry = new Log4jEntry(level, String.valueOf(msg));

                if(throwable != null) {
                    entry.setThrown(new ThrowableEntry(throwable));
                }
                if(getPref().isStackTraceEnabled()) {
                    entry.setStackTrace(EntryUtils.toEntry(Thread.currentThread().getStackTrace()));
                }

                ContextLogDispatcher.forward(getPref().getLog4jRegexLevel(category, level), id, entry);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getArgument(RegexMethodInterceptorEvent<Logger> evt, Class<T> type) {
        MethodInvocation invocation = evt.getInvocation();
        Method method = invocation.getMethod();

        if(method.getParameterTypes() == null) {
            return null;
        }

        for(int i = 0; i < method.getParameterTypes().length; i++) {
            if(type.equals(method.getParameterTypes()[i])) {
                return (T) invocation.getArguments()[i];
            }
        }

        return null;
    }
}