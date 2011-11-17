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

package ard.piraso.server.sql.logger;

import ard.piraso.api.sql.SQLParameterEntry;
import ard.piraso.server.proxy.RegexMethodInterceptorAdapter;
import ard.piraso.server.proxy.RegexMethodInterceptorEvent;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for collecting statement parameters.
 */
public class StatementParameterListener<T extends Statement> extends RegexMethodInterceptorAdapter<T> {

    protected static final List<String> VALID_METHOD_NAMES = Arrays.asList(
            "setBoolean",
            "setByte",
            "setShort",
            "setInt",
            "setLong",
            "setFloat",
            "setDouble",
            "setBigDecimal",
            "setString",
            "setBytes",
            "setDate",      // with cal
            "setTime",      // with cal
            "setTimestamp", // with cal
            "setRef",
            "setBlob",
            "setClob",
            "setArray",
            "setNull",      // with type
            "setURL",
            "setAsciiStream", // with length
            "setUnicodeStream",
            "setBinaryStream",
            "setCharacterStream",
            "setObject"        // with type
    );

    private Map<Integer, SQLParameterEntry> parameters = new LinkedHashMap<Integer, SQLParameterEntry>();

    public void clear() {
        parameters.clear();
    }

    public void addParameter(Integer index, SQLParameterEntry obj) {
        parameters.put(index, obj);
    }

    public Map<Integer, SQLParameterEntry> getParameters() {
        return parameters;
    }

    @Override
    public void beforeCall(RegexMethodInterceptorEvent<T> evt) {
        MethodInvocation invocation = evt.getInvocation();
        Method method = invocation.getMethod();

        if(!VALID_METHOD_NAMES.contains(method.getName())) {
            return;
        }

        Class[] types = method.getParameterTypes();
        if(Integer.TYPE.isAssignableFrom(types[0])) {
            Integer index = (Integer) invocation.getArguments()[0];

            addParameter(index, new SQLParameterEntry(index, method, invocation.getArguments()));
        }
    }
}
