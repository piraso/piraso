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

import org.springframework.remoting.support.RemoteInvocation;

import java.util.Arrays;
import java.util.List;

/**
 * Http remote invocation
 */
public class HttpRemoteInvocation {

    private String methodName;

    private List<Object> arguments;

    public HttpRemoteInvocation(RemoteInvocation invocation) {
        this.methodName = invocation.getMethodName();

        if(invocation.getArguments() != null && invocation.getArguments().length > 0) {
            arguments = Arrays.asList(invocation.getArguments());
        }
    }

    public List<Object> getArguments() {
        return arguments;
    }

    public String getMethodName() {
        return methodName;
    }
}
