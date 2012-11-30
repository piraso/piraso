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

package org.piraso.server.java.concurrent;

import org.piraso.api.entry.RequestEntry;
import org.piraso.api.entry.ResponseEntry;
import org.piraso.proxy.RegexMethodInterceptorAdapter;
import org.piraso.proxy.RegexMethodInterceptorEvent;
import org.piraso.proxy.RegexProxyFactory;
import org.piraso.server.GeneralPreferenceEvaluator;
import org.piraso.server.GroupChainId;
import org.piraso.server.PirasoContext;
import org.piraso.server.PirasoContextHolder;
import org.piraso.server.logger.AbstractLoggerProxyFactory;

import java.util.concurrent.ExecutorService;

/**
 * Proxy factory for {@link ExecutorService}
 */
public class ExecutorServiceLoggerProxyFactory extends AbstractLoggerProxyFactory<ExecutorService, GeneralPreferenceEvaluator> {

    public ExecutorServiceLoggerProxyFactory(GroupChainId id) {
        super(id, new RegexProxyFactory<ExecutorService>(ExecutorService.class), new GeneralPreferenceEvaluator());

        factory.addMethodListener("submit|execute", new SubmitHandler());
    }

    private class SubmitHandler extends RegexMethodInterceptorAdapter<ExecutorService> {

        @Override
        public void beforeCall(RegexMethodInterceptorEvent<ExecutorService> evt) {
            Object[] args = evt.getInvocation().getArguments();

            if(args.length > 0) {
                ExecutionEntryPoint entryPoint = new ExecutionEntryPoint(id.getGroupIds().getFirst());
                RequestEntry request = new RequestEntry(entryPoint.getPath());
                ResponseEntry response = new ResponseEntry();

                request.setServerName(entryPoint.getRemoteAddr());

                PirasoContext context = PirasoContextHolder.getContext();

                if(context != null) {
                    context = context.createChildContext(id);
                } else {
                    context = new PirasoContext(entryPoint);
                }

                Object[] replacement = new Object[args.length];

                for(int i = 0; i < args.length; i++) {
                    if(Runnable.class.isInstance(args[i])) {
                        replacement[i] = new PirasoContextRunnableWrapper((Runnable) args[i], context, request, response);
                    } else {
                        replacement[i] = args[i];
                    }
                }

                evt.setReplacedArguments(replacement);
            }
        }
    }
}
