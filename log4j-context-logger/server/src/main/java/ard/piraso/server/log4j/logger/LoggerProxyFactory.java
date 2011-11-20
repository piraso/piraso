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
import ard.piraso.api.log4j.Log4jEntry;
import ard.piraso.proxy.RegexMethodInterceptorAdapter;
import ard.piraso.proxy.RegexMethodInterceptorEvent;
import ard.piraso.proxy.RegexProxyFactory;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.dispatcher.ContextLogDispatcher;
import org.apache.log4j.Logger;

/**
 * ProxyFactory for {@link org.apache.log4j.Logger} class.
 */
public class LoggerProxyFactory  extends AbstractLog4jProxyFactory<Logger> {

    private String category;

    public LoggerProxyFactory(String category) {
        super(new RegexProxyFactory<Logger>(Logger.class));
        this.category = category;
        this.id = new GroupChainId(category);

        factory.addMethodListener("debug|error|fatal|info|warn|trace", new LevelLogInterceptorListener());
    }

    private class LevelLogInterceptorListener extends RegexMethodInterceptorAdapter<Logger> {
        @Override
        public void beforeCall(RegexMethodInterceptorEvent<Logger> evt) {
            String level = evt.getInvocation().getMethod().getName().toUpperCase();

            if(getPref().isLog4jEnabled()) {
                // we are on scope so start monitoring
                getPref().requestOnScope();
            }

            if(getPref().isLog4jEnabled(category, level)) {
                Object msg = evt.getInvocation().getArguments()[0];
                Log4jEntry entry = new Log4jEntry(level, String.valueOf(msg));

                if(getPref().isStackTraceEnabled()) {
                    entry.setStackTrace(EntryUtils.toEntry(Thread.currentThread().getStackTrace()));
                }

                ContextLogDispatcher.forward(id, entry);
            }
        }
    }
}