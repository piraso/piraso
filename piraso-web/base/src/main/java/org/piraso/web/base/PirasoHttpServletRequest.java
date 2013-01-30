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

package org.piraso.web.base;

import org.apache.commons.lang.Validate;
import org.codehaus.jackson.map.ObjectMapper;
import org.piraso.api.JacksonUtils;
import org.piraso.api.Preferences;
import org.piraso.server.PirasoEntryPoint;
import org.piraso.server.PirasoRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.piraso.api.PirasoConstants.*;

/**
 * Http implementation of {@link PirasoEntryPoint} and {@link PirasoRequest}.
 */
public class PirasoHttpServletRequest implements PirasoEntryPoint, PirasoRequest {

    private HttpServletRequest request;

    public PirasoHttpServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getPath() {
        return request.getRequestURI();
    }

    public Preferences getPreferences() {
        Validate.notNull(request.getParameter(PREFERENCES_PARAMETER), "");

        ObjectMapper mapper = JacksonUtils.createMapper();

        try {
            return mapper.readValue(request.getParameter(PREFERENCES_PARAMETER), Preferences.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public String getRemoteAddr() {
        if(request.getHeader(REMOTE_ADDRESS_HEADER) != null) {
            return request.getHeader(REMOTE_ADDRESS_HEADER);
        }

        return request.getRemoteAddr();
    }

    public String getWatchedAddr() {
        if(request.getParameter(WATCHED_ADDR_PARAMETER) != null) {
            return request.getParameter(WATCHED_ADDR_PARAMETER);
        }

        return request.getRemoteAddr();
    }

    public String getActivityUuid() {
        return request.getParameter(ACTIVITY_PARAMETER);
    }
}
