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

package org.piraso.server;

import org.piraso.api.Preferences;
import org.piraso.server.service.ResponseLoggerService;
import org.piraso.server.service.User;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.mockito.Mockito.*;

/**
 * Contains helper method for mocking common objects
 */
public class CommonMockObjects {
    public static TestPirasoRequest mockPirasoRequest(String monitoredAddr) {
        return new TestPirasoRequest(mockRequest(monitoredAddr));
    }

    public static TestPirasoRequest mockPirasoRequest(String monitoredAddr, String activityId) {
        return new TestPirasoRequest(mockRequest(monitoredAddr, activityId));
    }

    public static MockHttpServletRequest mockRequest(String monitoredAddr) {
        return mockRequest(monitoredAddr, null);
    }

    public static MockHttpServletRequest mockRequest(String monitoredAddr, String activityId) {
        MockHttpServletRequest request = spy(new MockHttpServletRequest());
        request.setRemoteAddr(monitoredAddr);

        if(activityId != null) {
            request.addParameter("activity_uuid", activityId);
        }

        return request;
    }

    public static ResponseLoggerService mockService(String monitoredAddr, boolean alive) {
        ResponseLoggerService service = mock(ResponseLoggerService.class);
        Preferences preferences = new Preferences();

        doReturn(preferences).when(service).getPreferences();
        doReturn(monitoredAddr).when(service).getWatchedAddr();
        doReturn(alive).when(service).isAlive();
        when(service.isWatched(monitoredAddr)).thenReturn(true);

        return service;
    }

    public static User createUser(String remoteAddr) {
        return createUser(remoteAddr, null);
    }

    public static User createUser(String remoteAddr, String activityId) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(remoteAddr);

        if(activityId != null) {
            request.addParameter("activity_uuid", activityId);
        }

        return new User(new TestPirasoRequest(request));
    }
}
