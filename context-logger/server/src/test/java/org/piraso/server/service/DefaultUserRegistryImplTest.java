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

package org.piraso.server.service;

import org.piraso.server.TestPirasoRequest;
import org.junit.Before;
import org.junit.Test;

import static org.piraso.server.CommonMockObjects.*;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link UserRegistry} class.
 */
public class DefaultUserRegistryImplTest {

    private UserRegistry registry;

    @Before
    public void setUp() throws Exception {
        registry = new DefaultUserRegistryImpl();
    }

    @Test
    public void testCreateUser() throws Exception {
        User expected = createUser("test", "a1");
        User actual = registry.createOrGetUser(mockPirasoRequest("test", "a1"));

        assertEquals(expected, actual);
    }

    @Test
    public void testAssociate() throws Exception {
        TestPirasoRequest request = mockPirasoRequest("test", "a1");

        User user = registry.createOrGetUser(request);
        ResponseLoggerService service = mockService(request.getRemoteAddr(), true);

        registry.associate(user, service);

        assertEquals(1, registry.getContextPreferences(request).size());
        assertEquals(1, registry.getContextLoggers(request).size());
        assertTrue(registry.isUserExist(user));
    }

    @Test
    public void testIsWatched() throws Exception {
        TestPirasoRequest request = mockPirasoRequest("test", "a1");

        assertFalse(registry.isWatched(request));

        User user = registry.createOrGetUser(request);
        ResponseLoggerService service = mockService(request.getRemoteAddr(), true);

        registry.associate(user, service);

        assertTrue(registry.isWatched(request));
    }

    @Test
    public void testRemove() throws Exception {
        TestPirasoRequest request = mockPirasoRequest("test", "a1");

        User user = registry.createOrGetUser(request);
        ResponseLoggerService service = mockService(request.getRemoteAddr(), true);

        registry.associate(user, service);

        assertEquals(1, registry.getContextPreferences(request).size());
        assertEquals(1, registry.getContextLoggers(request).size());
        assertTrue(registry.isUserExist(user));

        registry.removeUser(user);

        assertFalse(registry.isUserExist(user));
        assertTrue(registry.getContextLoggers(request).isEmpty());
        assertTrue(registry.getContextPreferences(request).isEmpty());
        verify(service, times(1)).stop();

        // removing non existing user
        registry.removeUser(user);
        assertFalse(registry.isUserExist(user));

        // still invoked once
        verify(service, times(1)).stop();
    }

    @Test
    public void testGetContext() throws Exception {
        TestPirasoRequest request = mockPirasoRequest("test", "a1");
        TestPirasoRequest orig = request;

        User user = registry.createOrGetUser(request);
        ResponseLoggerService service = mockService(request.getRemoteAddr(), true);

        registry.associate(user, service);

        assertEquals(1, registry.getContextPreferences(orig).size());
        assertEquals(1, registry.getContextLoggers(orig).size());

        // not valid url
        service.getPreferences().addUrlPattern("/someValidUrl");
        request.getMockRequest().setRequestURI("/notValid");

        assertEquals(0, registry.getContextPreferences(orig).size());
        assertEquals(0, registry.getContextLoggers(orig).size());

        // remove patterns
        service.getPreferences().setUrlPatterns(null);

        // not alive
        request = mockPirasoRequest("test2");
        User user2 = registry.createOrGetUser(request);
        ResponseLoggerService service2 = mockService(request.getRemoteAddr(), false);

        registry.associate(user2, service2);

        assertEquals(1, registry.getContextPreferences(orig).size());
        assertEquals(1, registry.getContextLoggers(orig).size());

        // not same monitored Addr and alive
        request = mockPirasoRequest("test3");
        User user3 = registry.createOrGetUser(request);
        ResponseLoggerService service3 = mockService(request.getRemoteAddr(), true);

        registry.associate(user3, service3);

        assertEquals(1, registry.getContextPreferences(orig).size());
        assertEquals(1, registry.getContextLoggers(orig).size());
    }
}
