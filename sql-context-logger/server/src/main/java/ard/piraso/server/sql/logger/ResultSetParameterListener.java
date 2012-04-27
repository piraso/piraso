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

package ard.piraso.server.sql.logger;

import ard.piraso.api.sql.SQLParameterEntry;
import ard.piraso.proxy.RegexMethodInterceptorAdapter;
import ard.piraso.proxy.RegexMethodInterceptorEvent;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.ArrayUtils;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * ResultSet parameter listener
 */
public class ResultSetParameterListener extends RegexMethodInterceptorAdapter<ResultSet> {
    List<SQLParameterEntry> parameters = new LinkedList<SQLParameterEntry>();

    boolean disabled = false;

    public void clear() {
        parameters.clear();
    }

    public void addParameter(SQLParameterEntry obj) {
        parameters.add(obj);
    }

    public List<SQLParameterEntry> getParameters() {
        return parameters;
    }

    public void disable() {
        disabled = true;
    }

    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void afterCall(RegexMethodInterceptorEvent<ResultSet> evt) {
        if(disabled) return;

        MethodInvocation invocation = evt.getInvocation();
        Method method = invocation.getMethod();
        Object returnedValue = evt.getReturnedValue();

        Class[] types = method.getParameterTypes();

        if(ArrayUtils.isEmpty(types)) {
            return;
        }

        if(Integer.TYPE.isAssignableFrom(types[0])) {
            Integer index = (Integer) invocation.getArguments()[0];

            addParameter(new SQLParameterEntry(index, method, invocation.getArguments(), returnedValue));
        } else if(String.class.isAssignableFrom(types[0])) {
            String str = (String) invocation.getArguments()[0];

            addParameter(new SQLParameterEntry(str, method, invocation.getArguments(), returnedValue));
        }
    }
}
