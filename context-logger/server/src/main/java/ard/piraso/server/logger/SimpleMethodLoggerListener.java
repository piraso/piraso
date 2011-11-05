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

package ard.piraso.server.logger;

import ard.piraso.api.Level;
import ard.piraso.api.entry.ElapseTimeEntry;
import ard.piraso.api.entry.MessageEntry;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.dispatcher.ContextLogDispatcher;
import ard.piraso.server.proxy.RegexMethodInterceptorEvent;
import ard.piraso.server.proxy.RegexMethodInterceptorListener;

/**
 * simple method call logger listener.
 */
public class SimpleMethodLoggerListener<T> implements RegexMethodInterceptorListener<T> {

    private GroupChainId id;

    private ElapseTimeEntry elapseTime;

    private MessageEntry entry;

    private Level level;

    public SimpleMethodLoggerListener(Level level, GroupChainId id) {
        this(level, id, null);
    }

    public SimpleMethodLoggerListener(Level level, GroupChainId id, ElapseTimeEntry elapseTime) {
        this.id = id;
        this.level = level;

        if(elapseTime == null) {
            this.elapseTime = new ElapseTimeEntry();
        } else {
            this.elapseTime = elapseTime;
        }
    }

    public void beforeCall(RegexMethodInterceptorEvent<T> evt) {
        entry = new MessageEntry(evt.getInvocation().getMethod().getName(), elapseTime);
        entry.getElapseTime().start();
    }

    public void afterCall(RegexMethodInterceptorEvent<T> evt) {
        entry.getElapseTime().stop();

        ContextLogDispatcher.forward(level, id, entry);
    }

    public void exceptionCall(RegexMethodInterceptorEvent<T> evt) {
        entry.getElapseTime().stop();
        entry.setMessage(evt.getInvocation().getMethod().getName() + ": " + evt.getException().getClass().getName());

        ContextLogDispatcher.forward(level, id, entry);
    }
}
