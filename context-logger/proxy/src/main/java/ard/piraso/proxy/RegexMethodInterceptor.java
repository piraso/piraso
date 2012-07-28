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

package ard.piraso.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Regular expression method interceptor.
 */
public class RegexMethodInterceptor<T> implements MethodInterceptor {

    private static final Logger LOG = Logger.getLogger(RegexMethodInterceptor.class);

    private Map<String, Set<RegexMethodInterceptorListener<T>>> listeners = new LinkedHashMap<String, Set<RegexMethodInterceptorListener<T>>>();

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Set<RegexMethodInterceptorListener<T>> invokeListeners = getListeners(invocation.getMethod());

        RegexMethodInterceptorEvent event = fireBeforeCall(invokeListeners, invocation);
        Object returnValue;

        try {
            // do replacement of arguments
            if(event != null && event.getReplacedArguments() != null &&
                    RegexProxyFactory.MethodInvocationWrapper.class.isInstance(invocation)) {
                RegexProxyFactory.MethodInvocationWrapper wrapper = (RegexProxyFactory.MethodInvocationWrapper) invocation;

                wrapper.setReplacedArguments(event.getReplacedArguments());
            }

            returnValue = invocation.proceed();
        } catch(Exception e) {
            fireExceptionCall(invokeListeners, invocation, e);

            throw e;
        }

        return fireAfterCall(invokeListeners, invocation, returnValue);
    }

    private RegexMethodInterceptorEvent fireBeforeCall(Set<RegexMethodInterceptorListener<T>> listeners, MethodInvocation invocation) {
        try {
            RegexMethodInterceptorEvent<T> event = new RegexMethodInterceptorEvent<T>(this, invocation);
            for(RegexMethodInterceptorListener<T> listener : listeners) {
                listener.beforeCall(event);
            }

            return event;
        } catch(Exception e) {
            LOG.warn(e.getMessage(), e);
        }

        return null;
    }

    private void fireExceptionCall(Set<RegexMethodInterceptorListener<T>> listeners, MethodInvocation invocation, Exception ex) {
        try {
            RegexMethodInterceptorEvent<T> event = new RegexMethodInterceptorEvent<T>(this, invocation);
            event.setException(ex);
            for(RegexMethodInterceptorListener<T> listener : listeners) {
                listener.exceptionCall(event);
            }
        } catch(Exception e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    private Object fireAfterCall(Set<RegexMethodInterceptorListener<T>> listeners, MethodInvocation invocation, Object returnedValue) {
        try {
            RegexMethodInterceptorEvent<T> event = new RegexMethodInterceptorEvent<T>(this, invocation, returnedValue);
            for(RegexMethodInterceptorListener<T> listener : listeners) {
                listener.afterCall(event);
            }

            return event.getReturnedValue();
        } catch(Exception e) {
            LOG.warn(e.getMessage(), e);
        }

        return returnedValue;
    }

    private Set<RegexMethodInterceptorListener<T>> getListeners(Method method) {
        Set<RegexMethodInterceptorListener<T>> matchListeners = new HashSet<RegexMethodInterceptorListener<T>>();

        String methodName = method.getName();
        for(Map.Entry<String, Set<RegexMethodInterceptorListener<T>>> entry : listeners.entrySet()) {
            if(methodName.matches(entry.getKey())) {
                matchListeners.addAll(entry.getValue());
            }
        }

        return matchListeners;
    }

    public void addAllMethodListener(Map<String, Set<RegexMethodInterceptorListener<T>>> newListeners) {
        for(Map.Entry<String, Set<RegexMethodInterceptorListener<T>>> entry : newListeners.entrySet()) {
            for(RegexMethodInterceptorListener<T> newListener : entry.getValue()) {
                addMethodListener(entry.getKey(), newListener);
            }
        }
    }

    public void addMethodListener(String regex, RegexMethodInterceptorListener<T> listener) {
        Validate.notNull("listener cannot be null.");

        Set<RegexMethodInterceptorListener<T>> set = listeners.get(regex);

        if(set == null) {
            set = new HashSet<RegexMethodInterceptorListener<T>>();
            listeners.put(regex, set);
        }

        set.add(listener);
    }
}
