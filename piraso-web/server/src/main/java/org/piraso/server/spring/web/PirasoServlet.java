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

package org.piraso.server.spring.web;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.piraso.api.JacksonUtils;
import org.piraso.api.entry.Entry;
import org.piraso.api.entry.RawEntry;
import org.piraso.server.PirasoContextIDGenerator;
import org.piraso.server.service.*;
import org.piraso.web.base.PirasoHttpServletRequest;
import org.piraso.web.base.PirasoHttpServletResponse;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static org.piraso.api.PirasoConstants.*;

/**
 * Entry point for requesting a starting and stopping log monitor activity.
 */
public class PirasoServlet implements HttpRequestHandler {

    /**
     * final class logging instance.
     */
    private static final Log LOG = LogFactory.getLog(PirasoServlet.class);

    public static final long DEFAULT_STOP_TIMEOUT = 10000l;

    private Integer maxQueueForceKillSize;

    private Long maxIdleTimeout;

    private Long stopTimeout = DEFAULT_STOP_TIMEOUT;

    private String version;

    public void setVersion(String version) {
        this.version = version;
    }

    public void setMaxIdleTimeout(Long maxIdleTimeout) {
        this.maxIdleTimeout = maxIdleTimeout;
    }

    public void setMaxQueueForceKillSize(Integer maxQueueForceKillSize) {
        this.maxQueueForceKillSize = maxQueueForceKillSize;
    }

    public void setStopTimeout(Long stopTimeout) {
        this.stopTimeout = stopTimeout;
    }

    public UserRegistry getRegistry() {
        return (UserRegistry) LoggerRegistrySingleton.INSTANCE.getRegistry();
    }

    private void writeResponse(HttpServletResponse response, String contentType, String str) throws IOException {
        PrintWriter out = response.getWriter();

        try {
            response.setContentType(contentType);
            response.setCharacterEncoding(ENCODING_UTF_8);

            out.write(str);
            out.flush();
        } finally {
            out.close();
        }
    }

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter(SERVICE_PARAMETER) == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request Parameter 'service' is required.");
            return;
        }

        User user = getRegistry().createOrGetUser(new PirasoHttpServletRequest(request));

        if(SERVICE_GET_REGISTRY_PARAMETER_VALUE.equals(request.getParameter(SERVICE_PARAMETER))) {
            retrieveRegistry(response);
        } else if(SERVICE_LOG_PARAMETER_VALUE.equals(request.getParameter(SERVICE_PARAMETER))) {
            log(request);
            writeResponse(response, PLAIN_CONTENT_TYPE, "OK");
        } else if(SERVICE_REQUEST_ID_PARAMETER_VALUE.equals(request.getParameter(SERVICE_PARAMETER))) {
            writeResponse(response, PLAIN_CONTENT_TYPE, String.valueOf(PirasoContextIDGenerator.INSTANCE.next()));
        } else if(SERVICE_START_PARAMETER_VALUE.equals(request.getParameter(SERVICE_PARAMETER))) {
            startLoggerService(request, response, user);
        } else if(SERVICE_STOP_PARAMETER_VALUE.equals(request.getParameter(SERVICE_PARAMETER))) {
            stopService(response, user);
            writeResponse(response, PLAIN_CONTENT_TYPE, "OK");
        } else if(SERVICE_TEST_PARAMETER_VALUE.equals(request.getParameter(SERVICE_PARAMETER))) {
            writeResponse(response, JSON_CONTENT_TYPE, String.format("{\"status\":\"%s\", \"version\":\"%s\", \"bridgeSupported\": true}", STATUS_OK, version));
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    String.format("Request Parameter 'service' with value '%s' is invalid.",
                            request.getParameter(SERVICE_PARAMETER)));
        }
    }

    private void retrieveRegistry(HttpServletResponse response) throws IOException {
        BridgeRegistry bridgeRegistry = new BridgeRegistry();

        for(Map.Entry<User, ResponseLoggerService> entry : getRegistry().getUserLoggerMap().entrySet()) {
            bridgeRegistry.addLogger(new BridgeLogger(entry.getValue()));
        }

        writeResponse(response, JSON_CONTENT_TYPE, JacksonUtils.MAPPER.writeValueAsString(bridgeRegistry));
    }

    private void log(HttpServletRequest request) throws IOException {
        String userContent = request.getParameter(USER_PARAMETER);
        String requestId = request.getParameter(ENTRY_REQUEST_ID_PARAMETER);
        String entryContent = request.getParameter(ENTRY_PARAMETER);
        String entryClassName = request.getParameter(ENTRY_CLASS_NAME_PARAMETER);

        Validate.notNull(userContent, "userContent should not be null.");

        User user = JacksonUtils.MAPPER.readValue(userContent, User.class);

        ResponseLoggerService service = getRegistry().getLogger(user);

        if(service != null && service.isAlive()) {
            Validate.notNull(entryContent, "entryContent should not be null.");
            Validate.notNull(entryClassName, "entryClassName should not be null.");

            Entry entry = new RawEntry(Long.valueOf(requestId), entryClassName, entryContent);
            service.log(entry);
        }
    }

    private void stopService(HttpServletResponse response, User user) throws IOException {
        ResponseLoggerService service = getRegistry().getLogger(user);

        if(service == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("Service for user '%s' not found.", user.toString()));
            return;
        }

        if(!service.isAlive()) {
            response.sendError(HttpServletResponse.SC_CONFLICT, String.format("Service for user '%s' not active.", user.toString()));
            getRegistry().removeUser(user);

            return;
        }

        try {
            // gracefully stop the service
            service.stopAndWait(stopTimeout);

            if(service.isAlive()) {
                response.sendError(HttpServletResponse.SC_REQUEST_TIMEOUT, String.format("Service for user '%s' stop timeout.", user.toString()));
            }
        } catch (InterruptedException ignored) {}
    }

    private void startLoggerService(HttpServletRequest request, HttpServletResponse response, User user) throws IOException, ServletException {
        ResponseLoggerServiceImpl service = new ResponseLoggerServiceImpl(
                user,
                new PirasoHttpServletRequest(request),
                new PirasoHttpServletResponse(response)
        );

        if(maxQueueForceKillSize != null) {
            service.setMaxQueueForceKillSize(maxQueueForceKillSize);
        }

        if(maxIdleTimeout != null) {
            service.setMaxIdleTimeout(maxIdleTimeout);
        }

        try {
            getRegistry().associate(user, service);
            service.start();
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        } finally {
            getRegistry().removeUser(user);
        }
    }
}
