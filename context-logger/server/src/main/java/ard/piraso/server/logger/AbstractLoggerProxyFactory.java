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

package ard.piraso.server.logger;

import ard.piraso.proxy.ProxyAware;
import ard.piraso.proxy.RegexProxyFactory;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.PreferenceEvaluator;

/**
 * Base class for logger factory.
 */
public abstract class AbstractLoggerProxyFactory<T, E extends PreferenceEvaluator> implements ProxyAware<T> {

    protected RegexProxyFactory<T> factory;

    protected T wrappedObject;

    protected GroupChainId id;

    protected E evaluator;

    public AbstractLoggerProxyFactory(GroupChainId id, RegexProxyFactory<T> factory, E evaluator) {
        this.factory = factory;
        this.id = id;
        this.evaluator = evaluator;
    }

    public T getProxy(T wrappedObject) {
        this.wrappedObject = wrappedObject;
        return factory.getProxy(wrappedObject);
    }

    public E getPref() {
        return evaluator;
    }
}
