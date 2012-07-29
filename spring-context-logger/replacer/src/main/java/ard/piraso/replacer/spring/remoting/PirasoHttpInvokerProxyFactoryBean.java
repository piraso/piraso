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

package ard.piraso.replacer.spring.remoting;

import ard.piraso.api.Level;
import ard.piraso.api.entry.ElapseTimeEntry;
import ard.piraso.api.entry.EntryUtils;
import ard.piraso.api.entry.MessageEntry;
import ard.piraso.api.entry.ThrowableEntry;
import ard.piraso.api.spring.SpringPreferenceEnum;
import ard.piraso.api.spring.SpringRemotingEndEntry;
import ard.piraso.api.spring.SpringRemotingStartEntry;
import ard.piraso.server.ContextPreference;
import ard.piraso.server.GeneralPreferenceEvaluator;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.PirasoEntryPointContext;
import ard.piraso.server.dispatcher.ContextLogDispatcher;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

/**
 * Piraso aware HttpInvokerProxyFactoryBean implementation.
 */
public class PirasoHttpInvokerProxyFactoryBean extends HttpInvokerProxyFactoryBean {
    private static final Log LOG = LogFactory.getLog(PirasoSimpleHttpInvokerRequestExecutor.class);

    protected ContextPreference context = new PirasoEntryPointContext();

    protected GeneralPreferenceEvaluator pref = new GeneralPreferenceEvaluator();

    @Override
    protected RemoteInvocationResult executeRequest(RemoteInvocation invocation, MethodInvocation originalInvocation) throws Exception {
        ElapseTimeEntry elapseTime = null;

        if (context.isMonitored()) {
            try {
                if (pref.isEnabled(SpringPreferenceEnum.REMOTING_ELAPSE_TIME_ENABLED)) {
                    elapseTime = new ElapseTimeEntry();
                    elapseTime.start();
                }

                if (pref.isEnabled(SpringPreferenceEnum.REMOTING_METHOD_CALL_ENABLED)) {
                    String declaringClass = originalInvocation.getMethod().getDeclaringClass().getName();
                    String methodName = originalInvocation.getMethod().getName();

                    Level level = Level.get(SpringPreferenceEnum.REMOTING_ENABLED.getPropertyName());
                    SpringRemotingStartEntry entry = new SpringRemotingStartEntry("START (" + methodName + "): " + originalInvocation.getMethod().toGenericString(), invocation);
                    entry.setMethodName(methodName);
                    entry.setMethodSignature(originalInvocation.getMethod().toGenericString());
                    entry.setUrl(getServiceUrl());
                    entry.setServiceInterface(getServiceInterface().getName());

                    if (pref.isStackTraceEnabled()) {
                        entry.setStackTrace(EntryUtils.toEntry(Thread.currentThread().getStackTrace()));
                    }

                    ContextLogDispatcher.forward(level, new GroupChainId(declaringClass), entry);
                }
            } catch (Exception e) {
                LOG.warn(e.getMessage(), e);
            }
        }

        RemoteInvocationResult result = super.executeRequest(invocation, originalInvocation);

        if (context.isMonitored()) {
            try {
                String declaringClass = originalInvocation.getMethod().getDeclaringClass().getName();
                String methodName = originalInvocation.getMethod().getName();

                if (elapseTime != null) {
                    elapseTime.stop();

                    Level level = Level.get(SpringPreferenceEnum.REMOTING_ENABLED.getPropertyName());
                    MessageEntry entry = new MessageEntry("Elapse Time", elapseTime);

                    ContextLogDispatcher.forward(level, new GroupChainId(declaringClass), entry);
                }

                if (context.isMonitored() && pref.isEnabled(SpringPreferenceEnum.REMOTING_METHOD_CALL_ENABLED)) {
                    Level level = Level.get(SpringPreferenceEnum.REMOTING_ENABLED.getPropertyName());
                    SpringRemotingEndEntry entry = new SpringRemotingEndEntry("END (" + methodName + "): Returned Value ", result);
                    entry.setMethodName(methodName);
                    entry.setUrl(getServiceUrl());
                    entry.setServiceInterface(getServiceInterface().getName());

                    if (result.getException() != null) {
                        entry.setThrown(new ThrowableEntry(result.getException()));
                    }

                    ContextLogDispatcher.forward(level, new GroupChainId(declaringClass), entry);
                }
            } catch (Exception e) {
                LOG.warn(e.getMessage(), e);
            }
        }

        return result;
    }
}
