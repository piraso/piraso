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

package ard.piraso.proxy;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.DefaultAopProxyFactory;
import org.springframework.aop.framework.ProxyFactory;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Regular expression proxy factory.
 */
public class RegexProxyFactory<T> implements ProxyAware<T> {

    private Class<T> clazz;

    private Map<String, Set<RegexMethodInterceptorListener<T>>> listeners = new HashMap<String, Set<RegexMethodInterceptorListener<T>>>();

    public RegexProxyFactory(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T getProxy(T object) {
        ProxyInterceptorAware<T> proxy = getProxyInterceptor(object);

        return proxy.getProxy();
    }

    @SuppressWarnings("unchecked")
    public ProxyInterceptorAware<T> getProxyInterceptor(T object) {
        RegexMethodInterceptor<T> wrapper = new RegexMethodInterceptorWrapper(new RegexMethodInterceptor<T>(), object);
        wrapper.addAllMethodListener(listeners);

        T proxy;

        if(clazz.isInterface()) {
            proxy = ProxyFactory.getProxy(clazz, wrapper);
        } else {
            AdvisedSupport advisedSupport = new AdvisedSupport();
            advisedSupport.setTarget(object);
            advisedSupport.addAdvice(wrapper);
            AopProxy aopProxy = new DefaultAopProxyFactory().createAopProxy(advisedSupport);
            proxy = (T) aopProxy.getProxy();
        }

        return new ProxyInterceptorAware<T>(proxy, wrapper);
    }

    public void addMethodListener(String regex, RegexMethodInterceptorListener<T> listener) {
        if(listener == null) {
            throw new IllegalArgumentException("listener cannot be null.");
        }

        Set<RegexMethodInterceptorListener<T>> set = listeners.get(regex);
        if(set == null) {
            set = new HashSet<RegexMethodInterceptorListener<T>>();
            listeners.put(regex, set);
        }
        
        set.add(listener);
    }
    
    private class RegexMethodInterceptorWrapper extends RegexMethodInterceptor<T> {

        private RegexMethodInterceptor<T> wrappedObject;

        private T target;

        private RegexMethodInterceptorWrapper(RegexMethodInterceptor<T> wrappedObject, T target) {
            this.wrappedObject = wrappedObject;
            this.target = target;
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            return wrappedObject.invoke(new MethodInvocationWrapper(invocation, target));
        }

        @Override
        public void addAllMethodListener(Map<String, Set<RegexMethodInterceptorListener<T>>> newListeners) {
            wrappedObject.addAllMethodListener(newListeners);
        }

        @Override
        public void addMethodListener(String regex, RegexMethodInterceptorListener<T> tRegexMethodInterceptorListener) {
            wrappedObject.addMethodListener(regex, tRegexMethodInterceptorListener);
        }
    }

    private class MethodInvocationWrapper implements MethodInvocation {
        private MethodInvocation wrappedObject;

        private T target;

        private MethodInvocationWrapper(MethodInvocation wrappedObject, T target) {
            this.wrappedObject = wrappedObject;
            this.target = target;
        }

        public Method getMethod() {
            return wrappedObject.getMethod();
        }

        public Object[] getArguments() {
            return wrappedObject.getArguments();
        }

        public Object proceed() throws Throwable {
            Method method = getMethod();

            return method.invoke(target, getArguments());
        }

        public Object getThis() {
            return target;
        }

        public AccessibleObject getStaticPart() {
            return wrappedObject.getStaticPart();
        }
    }
}
