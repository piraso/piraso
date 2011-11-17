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

package ard.piraso.server;

import ard.piraso.server.service.ResponseLoggerService;
import ard.piraso.server.service.ResponseLoggerServiceImpl;
import ard.piraso.server.service.User;
import ard.piraso.server.service.UserRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Entry point for requesting a starting and stopping log monitor activity.
 */
public class PirasoServlet implements HttpRequestHandler {

    /**
     * final class logging instance.
     */
    private static final Log LOG = LogFactory.getLog(PirasoServlet.class);

    private static final String START_OPERATION = "start";

    private static final String STOP_OPERATION = "stop";

    private static final String SERVICE_PARAMETER = "service";

    public static final long DEFAULT_STOP_TIMEOUT = 10000l;

    private Integer maxQueueForceKillSize;

    private Long maxIdleTimeout;

    private Long stopTimeout = DEFAULT_STOP_TIMEOUT;

    private UserRegistry registry;

    public void setRegistry(UserRegistry registry) {
        this.registry = registry;
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

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter(SERVICE_PARAMETER) == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request Parameter 'service' is required.");
            return;
        }

        User user = registry.createOrGetUser(request);

        if(START_OPERATION.equals(request.getParameter(SERVICE_PARAMETER))) {
            startLoggerService(request, response, user);
        } else if(STOP_OPERATION.equals(request.getParameter(SERVICE_PARAMETER))) {
            stopService(response, user);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    String.format("Request Parameter 'service' with value '%s' is invalid.",
                            request.getParameter(SERVICE_PARAMETER)));
        }
    }

    private void stopService(HttpServletResponse response, User user) throws IOException {
        ResponseLoggerService service = registry.getLogger(user);

        if(service == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("Service for user '%s' not found.", user.toString()));
            return;
        }

        if(!service.isAlive()) {
            response.sendError(HttpServletResponse.SC_CONFLICT, String.format("Service for user '%s' not active.", user.toString()));
            registry.removeUser(user);

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
        ResponseLoggerServiceImpl service = new ResponseLoggerServiceImpl(user, request, response);

        if(maxQueueForceKillSize != null) {
            service.setMaxQueueForceKillSize(maxQueueForceKillSize);
        }

        if(maxIdleTimeout != null) {
            service.setMaxIdleTimeout(maxIdleTimeout);
        }

        try {
            registry.associate(user, service);
            service.start();
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        } finally {
            registry.removeUser(user);
        }
    }
}
