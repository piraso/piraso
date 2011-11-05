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

import ard.piraso.api.GeneralPreferenceEnum;
import ard.piraso.api.Level;
import ard.piraso.api.entry.Entry;
import ard.piraso.api.entry.MessageEntry;
import ard.piraso.server.service.ResponseLoggerService;
import ard.piraso.server.service.User;
import ard.piraso.server.service.UserRegistry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static ard.piraso.server.CommonMockObjects.mockRequest;
import static ard.piraso.server.CommonMockObjects.mockService;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test for {@link PirasoContext} class.
 */
public class PirasoContextTest {

    public static final String MONITORED_ADDR = "127.0.0.1";

    private UserRegistry registry;

    private MockHttpServletRequest request;

    private PirasoContext context;

    @Before
    public void setUp() throws Exception {
        registry = spy(new UserRegistry());
        request = mockRequest(MONITORED_ADDR);
        context = new PirasoContext(request, registry);
    }

    @Test
    public void testIsMonitoredNoAssociatedUser() throws Exception {
        assertFalse(context.isMonitored());
    }

    @Test
    public void testIsMonitoredException() throws Exception {
        doThrow(new IOException()).when(registry).getContextPreferences(request);

        assertFalse(context.isMonitored());
    }

    @Test
    public void testIsMonitoredWithAssociatedUser() throws Exception {
        associateUser(request);
        assertTrue(context.isMonitored());
    }

    @Test
    public void testIsEnabledNoAssociatedUser() throws Exception {
        assertFalse(context.isEnabled("any property"));
    }

    @Test
    public void testIsEnabledIOException() throws Exception {
        doThrow(new IOException()).when(registry).getContextPreferences(request);

        assertFalse(context.isEnabled("any property"));
    }

    @Test
    public void testIsEnabledWithProperty() throws Exception {
        User user = associateUser(request);
        ResponseLoggerService service = registry.getLogger(user);

        service.getPreferences().addProperty("enabledProperty", true);
        service.getPreferences().addProperty("disabledProperty", false);

        assertTrue(context.isEnabled("enabledProperty"));
        assertFalse(context.isEnabled("disabledProperty"));
    }

    @Test
    public void testGetIntValueNoAssociatedUser() throws Exception {
        assertNull(context.getIntValue("any property"));
    }

    @Test
    public void testGetIntValueIOException() throws Exception {
        doThrow(new IOException()).when(registry).getContextPreferences(request);

        assertNull(context.getIntValue("any property"));
    }

    @Test
    public void testGetIntValueWithProperty() throws Exception {
        User user = associateUser(request);
        ResponseLoggerService service = registry.getLogger(user);

        service.getPreferences().addProperty("1Property", 1);
        service.getPreferences().addProperty("2Property", 2);

        assertEquals(Integer.valueOf(1), context.getIntValue("1Property"));
        assertEquals(Integer.valueOf(2), context.getIntValue("2Property"));
        assertNull(context.getIntValue("3Property"));

        MockHttpServletRequest request2 = mockRequest(MONITORED_ADDR);

        User user2 = associateUser(request2);
        ResponseLoggerService service2 = registry.getLogger(user2);
        service2.getPreferences().addProperty("1Property", 21);
        service2.getPreferences().addProperty("2Property", 22);

        MockHttpServletRequest request3 = mockRequest(MONITORED_ADDR);
        User user3 = associateUser(request3);
        ResponseLoggerService service3 = registry.getLogger(user3);
        service3.getPreferences().addProperty("1Property", 11);

        assertEquals(Integer.valueOf(21), context.getIntValue("1Property"));
        assertEquals(Integer.valueOf(22), context.getIntValue("2Property"));
        assertNull(context.getIntValue("3Property"));
    }

    @Test
    public void testLogNoAssociatedUser() throws Exception {
        context.log(Level.ALL, new GroupChainId("test"), new MessageEntry("test"));

        // this will not be invoked since no associated users yet
        verify(registry, times(0)).getContextLoggers(request);
    }

    @Test
    public void testLogWithVaryingEnabledProperty() throws Exception {
        User user = associateUser(request);
        ResponseLoggerService service = registry.getLogger(user);

        service.getPreferences().addProperty("varyingProperty", true);

        MockHttpServletRequest request2 = mockRequest(MONITORED_ADDR);
        User user2 = associateUser(request2);
        ResponseLoggerService service2 = registry.getLogger(user2);
        service2.getPreferences().addProperty("varyingProperty", false);

        GroupChainId id = new GroupChainId("test");
        MessageEntry entry = new MessageEntry("test");

        Level.addLevel("varyingProperty");

        context.log(Level.get("varyingProperty"), id, entry);

        verify(service, times(1)).log(entry);
        verify(service2, times(0)).log(entry);
    }

    @Test
    public void testLogNullProperty() throws Exception {
        User user = associateUser(request);

        MockHttpServletRequest request2 = mockRequest(MONITORED_ADDR);
        User user2 = associateUser(request2);

        GroupChainId id = new GroupChainId("test");
        MessageEntry entry = new MessageEntry("test");

        context.requestOnScope();
        context.log(null, id, entry);

        verify(registry.getLogger(user), times(1)).log(entry);
        verify(registry.getLogger(user2), times(1)).log(entry);
    }

    @Test
    public void testLogQueueEntry() throws IOException {
        User user = associateUser(request);
        ResponseLoggerService service = registry.getLogger(user);

        service.getPreferences().addProperty(GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName(), true);

        GroupChainId id = new GroupChainId("test");
        MessageEntry entry = new MessageEntry("test");

        User user2 = associateUser(request);
        ResponseLoggerService service2 = registry.getLogger(user2);

        context.log(Level.SCOPED, id, entry);

        verify(service, times(0)).log(entry);
        verify(service2, times(1)).log(entry);
    }

    @Test
    public void testLogQueueEntryWithRequestOnScope() throws IOException {
        User user = associateUser(request);
        ResponseLoggerService service = registry.getLogger(user);

        service.getPreferences().addProperty(GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName(), true);

        GroupChainId id = new GroupChainId("test");
        MessageEntry entry = new MessageEntry("test");

        User user2 = associateUser(request);
        ResponseLoggerService service2 = registry.getLogger(user2);

        MockHttpServletRequest request3 = mockRequest(MONITORED_ADDR);

        User user3 = associateUser(request3);
        ResponseLoggerService service3 = registry.getLogger(user3);
        service3.getPreferences().addProperty(GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName(), false);

        context.log(Level.SCOPED, id, entry);

        verify(service, times(0)).log(entry);
        verify(service2, times(1)).log(entry);

        context.log(null, id, entry);

        verify(service, times(2)).log(entry);
        verify(service2, times(2)).log(entry);
    }

    @Test
    public void testLogQueueEntryWithRequestOnScopeAllNoneScopeEnabled() throws IOException {
        User user = associateUser(request);
        ResponseLoggerService service = registry.getLogger(user);

        service.getPreferences().addProperty(GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName(), false);

        GroupChainId id = new GroupChainId("test");
        MessageEntry entry = new MessageEntry("test");

        User user2 = associateUser(request);
        ResponseLoggerService service2 = registry.getLogger(user2);

        MockHttpServletRequest request3 = mockRequest(MONITORED_ADDR);

        User user3 = associateUser(request3);
        ResponseLoggerService service3 = registry.getLogger(user3);
        service3.getPreferences().addProperty(GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName(), false);

        doThrow(new IOException()).when(service3).log(Matchers.<Entry>any());

        context.log(Level.SCOPED, id, entry);

        // all should be invoked since all are non-scoped aware
        verify(service, times(1)).log(entry);
        verify(service2, times(1)).log(entry);
        verify(service3, times(1)).log(entry);
    }

    @Test
    public void testLogQueueEntryWithRequestOnScopeAllNoneScopeEnabledAlreadyOnScoped() throws IOException {
        User user = associateUser(request);
        ResponseLoggerService service = registry.getLogger(user);

        service.getPreferences().addProperty(GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName(), false);

        GroupChainId id = new GroupChainId("test");
        MessageEntry entry = new MessageEntry("test");

        User user2 = associateUser(request);
        ResponseLoggerService service2 = registry.getLogger(user2);

        MockHttpServletRequest request3 = mockRequest(MONITORED_ADDR);

        User user3 = associateUser(request3);
        ResponseLoggerService service3 = registry.getLogger(user3);
        service3.getPreferences().addProperty(GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName(), false);

        doThrow(new IOException()).when(service3).log(Matchers.<Entry>any());

        context.requestOnScope();
        context.log(Level.SCOPED, id, entry);

        // all should be invoked since all are non-scoped aware
        verify(service, times(1)).log(entry);
        verify(service2, times(1)).log(entry);
        verify(service3, times(1)).log(entry);
    }

    private User associateUser(MockHttpServletRequest request) throws IOException {
        User user = registry.createOrGetUser(request);
        ResponseLoggerService service = mockService(request.getRemoteAddr(), true);

        registry.associate(user, service);

        return user;
    }
}
