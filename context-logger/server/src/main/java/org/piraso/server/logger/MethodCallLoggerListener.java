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

package org.piraso.server.logger;

import org.piraso.api.Level;
import org.piraso.api.entry.*;
import org.piraso.proxy.RegexMethodInterceptorEvent;
import org.piraso.proxy.RegexMethodInterceptorListener;
import org.piraso.server.GeneralPreferenceEvaluator;
import org.piraso.server.GroupChainId;
import org.piraso.server.dispatcher.ContextLogDispatcher;

/**
 * Method call logger listener.
 */
public class MethodCallLoggerListener<T> implements RegexMethodInterceptorListener<T> {

    private ElapseTimeEntry elapseTime = new ElapseTimeEntry();

    private MethodCallEntry entry;

    private GroupChainId id;

    private GeneralPreferenceEvaluator preference;

    private Level level;

    public MethodCallLoggerListener(Level level, GroupChainId id) {
        this.id = id;
        this.preference = new GeneralPreferenceEvaluator();
        this.level = level;
    }

    public void beforeCall(RegexMethodInterceptorEvent<T> evt) {
        entry = new MethodCallEntry(evt.getInvocation().getMethod(), elapseTime);

        Object[] arguments = evt.getInvocation().getArguments();
        entry.setArguments(EntryUtils.toEntry(arguments));

        // method stack trace only if debug is enabled
        if(preference.isStackTraceEnabled()) {
            entry.setStackTrace(EntryUtils.toEntry(Thread.currentThread().getStackTrace()));
        }

        elapseTime.start();
    }

    public void afterCall(RegexMethodInterceptorEvent<T> evt) {
        elapseTime.stop();
        entry.setReturnedValue(new ObjectEntry(evt.getReturnedValue()));

        ContextLogDispatcher.forward(level, id, entry);
    }

    public void exceptionCall(RegexMethodInterceptorEvent<T> evt) {
        entry.setThrown(new ThrowableEntry(evt.getException()));
        elapseTime.stop();

        ContextLogDispatcher.forward(level, id, entry);
    }
}
