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

package org.piraso.server.spring.remoting;

import org.piraso.server.ContextPreference;
import org.piraso.server.PirasoEntryPointContext;
import org.piraso.server.spring.logger.SpringRemotingProxyFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.remoting.httpinvoker.HttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.piraso.api.PirasoConstants.*;

/**
 * Piraso {@link HttpInvokerRequestExecutor} wrapper which ensures piraso context properly propagated to the invoked
 * http request executor.
 */
public class HttpInvokerRequestExecutorWrapper implements HttpInvokerRequestExecutor {

    /**
     * final class logging instance.
     */
    private static final Log LOG = LogFactory.getLog(HttpInvokerRequestExecutorWrapper.class);

    private ContextPreference pirasoContext = new PirasoEntryPointContext();

    private HttpInvokerRequestExecutor delegate;

    private HttpInvokerReflectionHelper helper;

    public HttpInvokerRequestExecutorWrapper(HttpInvokerRequestExecutor delegate) {
        this.delegate = delegate;
        if (SimpleHttpInvokerRequestExecutor.class.isInstance(delegate)) {
            helper = new HttpInvokerReflectionHelper((SimpleHttpInvokerRequestExecutor) delegate);
        }
    }

    private String getGroupId() {
        return (String) pirasoContext.getProperty(SpringRemotingProxyFactory.class, "groupId");
    }

    public RemoteInvocationResult executeRequest(HttpInvokerClientConfiguration config, RemoteInvocation invocation) throws Exception {
        if (helper != null && pirasoContext.isMonitored()) {
            try {
                ByteArrayOutputStream baos = helper.getByteArrayOutputStream(invocation);

                HttpURLConnection con = helper.openConnection(config);
                helper.prepareConnection(con, baos.size());
                con.setRequestProperty(REMOTE_ADDRESS_HEADER, pirasoContext.getEntryPoint().getRemoteAddr());
                con.setRequestProperty(REQUEST_ID_HEADER, String.valueOf(pirasoContext.getRequestId()));

                String groupId = getGroupId();
                if (groupId != null) {
                    con.setRequestProperty(GROUP_ID_HEADER, groupId);
                }

                helper.writeRequestBody(config, con, baos);
                helper.validateResponse(config, con);
                InputStream responseBody = helper.readResponseBody(config, con);

                return helper.readRemoteInvocationResult(responseBody, config.getCodebaseUrl());
            } catch (HttpInvokerReflectionException e) {
                LOG.warn("Error on propagating piraso context. Will revert to direct delegation.", e);
            }
        }

        return delegate.executeRequest(config, invocation);
    }
}
