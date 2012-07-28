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

package ard.piraso.api.entry;

import java.lang.reflect.Method;

/**
 * Defines a method call log entry.
 */
public class MethodCallEntry extends Entry implements ElapseTimeAware, ThrowableAwareEntry, StackTraceAwareEntry {

    private String methodName;
    
    private String genericString;

    private String[] parameterClassNames;

    private ObjectEntry[] arguments;

    private ObjectEntry returnedValue;

    private String returnClassName;

    private ElapseTimeEntry elapseTime;

    private ThrowableEntry thrown;

    private StackTraceElementEntry[] stackTrace;

    public MethodCallEntry() {
    }

    public MethodCallEntry(Method method) {
        this(method, null);
    }

    public MethodCallEntry(Method method, ElapseTimeEntry elapseTime) {
        this(method, elapseTime, null);
    }

    public MethodCallEntry(Method method, ElapseTimeEntry elapseTime, ThrowableEntry thrown) {
        this.elapseTime = elapseTime;
        this.thrown = thrown;

        init(method);
    }

    public void init(Method method) {
        methodName = method.getName();
        genericString = method.toGenericString();
        returnClassName = method.getReturnType().getName();

        parameterClassNames = new String[method.getParameterTypes().length];
        for(int i = 0; i < method.getParameterTypes().length; i++) {
            parameterClassNames[i] = method.getParameterTypes()[i].getName();
        }
    }

    public ObjectEntry[] getArguments() {
        return arguments;
    }

    public void setArguments(ObjectEntry[] arguments) {
        this.arguments = arguments;
    }

    public ObjectEntry getReturnedValue() {
        return returnedValue;
    }

    public void setReturnedValue(ObjectEntry returnedValue) {
        this.returnedValue = returnedValue;
    }

    public StackTraceElementEntry[] getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTraceElementEntry[] stackTrace) {
        this.stackTrace = stackTrace;
    }

    public ThrowableEntry getThrown() {
        return thrown;
    }

    public void setThrown(ThrowableEntry thrown) {
        this.thrown = thrown;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getParameterClassNames() {
        return parameterClassNames;
    }

    public void setParameterClassNames(String[] parameterClassNames) {
        this.parameterClassNames = parameterClassNames;
    }

    public String getReturnClassName() {
        return returnClassName;
    }

    public void setReturnClassName(String returnClassName) {
        this.returnClassName = returnClassName;
    }

    public ElapseTimeEntry getElapseTime() {
        return elapseTime;
    }

    public void setElapseTime(ElapseTimeEntry elapseTime) {
        this.elapseTime = elapseTime;
    }

    public String getGenericString() {
        return genericString;
    }

    public void setGenericString(String genericString) {
        this.genericString = genericString;
    }
}
