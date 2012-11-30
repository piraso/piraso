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

package org.piraso.server.spring.logger;

import org.piraso.api.Level;
import org.piraso.api.entry.*;
import org.piraso.api.spring.SpringPreferenceEnum;
import org.piraso.proxy.RegexMethodInterceptorAdapter;
import org.piraso.proxy.RegexMethodInterceptorEvent;
import org.piraso.proxy.RegexProxyFactory;
import org.piraso.server.GroupChainId;
import org.piraso.server.dispatcher.ContextLogDispatcher;
import org.springframework.remoting.httpinvoker.HttpInvokerRequestExecutor;

/**
 * Spring remoting proxy factory
 */
public class SpringRemotingProxyFactory extends AbstractSpringProxyFactory<HttpInvokerRequestExecutor> {

    private static final Level METHOD_CALL_LEVEL = Level.get(SpringPreferenceEnum.REMOTING_METHOD_CALL_ENABLED.getPropertyName());

    private static final Level BASE_LEVEL = Level.get(SpringPreferenceEnum.REMOTING_ENABLED.getPropertyName());

    public SpringRemotingProxyFactory(GroupChainId id) {
        super(id, new RegexProxyFactory<HttpInvokerRequestExecutor>(HttpInvokerRequestExecutor.class));

        factory.addMethodListener("executeRequest", new ExecuteRequestListener());
    }

    private class ExecuteRequestListener extends RegexMethodInterceptorAdapter<HttpInvokerRequestExecutor> {

        @Override
        public void beforeCall(RegexMethodInterceptorEvent<HttpInvokerRequestExecutor> evt) {
            if (getPref().isSpringRemotingEnabled()) {
                ElapseTimeEntry entry = new ElapseTimeEntry();
                entry.start();

                getPref().getContext().addProperty(SpringRemotingProxyFactory.class, "elapse", entry);
                getPref().getContext().addProperty(SpringRemotingProxyFactory.class, "groupId", id.toString());
            }
        }

        @Override
        public void afterCall(RegexMethodInterceptorEvent<HttpInvokerRequestExecutor> evt) {
            if (getPref().isSpringRemotingEnabled()) {
                if (getPref().isSpringRemotingMethodCallEnabled()) {
                    MethodCallEntry entry = createMethodEntry(evt);
                    entry.setReturnedValue(new ObjectEntry(evt.getReturnedValue()));

                    ContextLogDispatcher.forward(METHOD_CALL_LEVEL, id, entry);
                }

                MessageEntry entry = createMessageEntry(evt);

                ContextLogDispatcher.forward(BASE_LEVEL, id, entry);
            }
        }

        @Override
        public void exceptionCall(RegexMethodInterceptorEvent<HttpInvokerRequestExecutor> evt) {
            if (getPref().isSpringRemotingEnabled()) {
                if (getPref().isSpringRemotingMethodCallEnabled()) {
                    MethodCallEntry entry = createMethodEntry(evt);
                    entry.setThrown(new ThrowableEntry(evt.getException()));

                    ContextLogDispatcher.forward(METHOD_CALL_LEVEL, id, entry);
                }

                MessageEntry entry = createMessageEntry(evt);
                entry.setMessage(evt.getInvocation().getMethod().getName() + ": " + evt.getException().getClass().getName());

                ContextLogDispatcher.forward(BASE_LEVEL, id, entry);
            }
        }
    }


    private MethodCallEntry createMethodEntry(RegexMethodInterceptorEvent<HttpInvokerRequestExecutor> evt) {
        ElapseTimeEntry elapseTime = (ElapseTimeEntry) getPref().getContext().getProperty(SpringRemotingProxyFactory.class, "elapse");
        elapseTime.stop();

        MethodCallEntry entry = new MethodCallEntry(evt.getInvocation().getMethod(), elapseTime);

        Object[] arguments = evt.getInvocation().getArguments();
        entry.setArguments(EntryUtils.toEntry(arguments));

        // method stack trace only if debug is enabled
        if (getPref().isStackTraceEnabled()) {
            entry.setStackTrace(EntryUtils.toEntry(Thread.currentThread().getStackTrace()));
        }

        return entry;
    }

    private MessageEntry createMessageEntry(RegexMethodInterceptorEvent<HttpInvokerRequestExecutor> evt) {
        ElapseTimeEntry elapseTime = (ElapseTimeEntry) getPref().getContext().getProperty(SpringRemotingProxyFactory.class, "elapse");
        elapseTime.stop();

        return new MessageEntry(evt.getInvocation().getMethod().getName(), elapseTime);
    }
}
