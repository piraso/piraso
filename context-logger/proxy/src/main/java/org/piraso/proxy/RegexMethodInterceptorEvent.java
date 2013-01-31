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

package org.piraso.proxy;

import org.aopalliance.intercept.MethodInvocation;

/**
 * Method interceptor event.
 */
public class RegexMethodInterceptorEvent<T>  {

    private MethodInvocation invocation;

    private T target;

    private RegexMethodInterceptor<T> source;

    private Object returnedValue;

    private Object setReturnedValue;

    private Exception exception;

    private Object[] replacedArguments;

    private boolean wasSetReturnedValue;

    private boolean skip;
    
    RegexMethodInterceptorEvent(RegexMethodInterceptor<T> source, MethodInvocation invocation) {
        this(source, invocation, null);
    }

    @SuppressWarnings("unchecked")
    RegexMethodInterceptorEvent(RegexMethodInterceptor<T> source, MethodInvocation invocation, Object returnedValue) {
        this.source = source;
        this.invocation = invocation;
        this.target = (T) invocation.getThis();
        this.returnedValue = returnedValue;
    }

    public void setReplacedArguments(Object[] replacedArguments) {
        this.replacedArguments = replacedArguments;
    }

    public Object[] getReplacedArguments() {
        return replacedArguments;
    }

    public Exception getException() {
        return exception;
    }

    void setException(Exception exception) {
        this.exception = exception;
    }

    public MethodInvocation getInvocation() {
        return invocation;
    }

    public T getTarget() {
        return target;
    }

    public RegexMethodInterceptor<T> getSource() {
        return source;
    }

    public boolean isSkip() {
        return skip;
    }

    public void skip() {
        this.skip = true;
    }

    public Object getReturnedValue() {
        if(wasSetReturnedValue) {
            return setReturnedValue;
        }

        return returnedValue;
    }

    public void setReturnedValue(Object newReturnedValue) {
        if(wasSetReturnedValue) {
            throw new IllegalStateException("A previous listener 'returnedValue' was already set.");
        }
        this.wasSetReturnedValue = true;
        this.setReturnedValue = newReturnedValue;
    }
}
