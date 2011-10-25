package ard.piraso.server.service;

import ard.piraso.api.Preferences;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test for {@link UserRegistry} class.
 */
public class UserRegistryTest {

    private UserRegistry registry;

    @Before
    public void setUp() throws Exception {
        registry = new UserRegistry();
    }

    @Test
    public void testCreateUser() throws Exception {
        User expected = UserTest.createUser("test", "a1");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("test");
        request.addParameter("activity_uuid", "a1");

        User actual = registry.createOrGetUser(request);

        assertEquals(expected, actual);
    }

    @Test
    public void testAssociate() throws Exception {
        final String monitoredAddr = "test";
        User user = UserTest.createUser(monitoredAddr);
        ResponseLoggerService service = mockService(monitoredAddr, true);

        registry.associate(user, service);

        assertEquals(1, registry.getContextPreferences(monitoredAddr).size());
        assertEquals(1, registry.getContextLoggers(monitoredAddr).size());
        assertTrue(registry.isUserExist(user));
    }

    @Test
    public void testRemove() throws Exception {
        final String monitoredAddr = "test";
        User user = UserTest.createUser(monitoredAddr);
        ResponseLoggerService service = mockService(monitoredAddr, true);

        registry.associate(user, service);

        assertEquals(1, registry.getContextPreferences(monitoredAddr).size());
        assertEquals(1, registry.getContextLoggers(monitoredAddr).size());
        assertTrue(registry.isUserExist(user));

        registry.removeUser(user);

        assertFalse(registry.isUserExist(user));
        assertTrue(registry.getContextLoggers(monitoredAddr).isEmpty());
        assertTrue(registry.getContextPreferences(monitoredAddr).isEmpty());
        verify(service, times(1)).stop();

        // removing non existing user
        registry.removeUser(user);
        assertFalse(registry.isUserExist(user));

        // still invoked once
        verify(service, times(1)).stop();
    }

    @Test
    public void testGetContext() throws Exception {
        final String monitoredAddr = "test";
        User user = UserTest.createUser(monitoredAddr);
        ResponseLoggerService service = mockService(monitoredAddr, true);

        registry.associate(user, service);

        assertEquals(1, registry.getContextPreferences(monitoredAddr).size());
        assertEquals(1, registry.getContextLoggers(monitoredAddr).size());

        // not alive
        user = UserTest.createUser("test2");
        service = mockService(monitoredAddr, false);

        registry.associate(user, service);

        assertEquals(1, registry.getContextPreferences(monitoredAddr).size());
        assertEquals(1, registry.getContextLoggers(monitoredAddr).size());

        // not same monitored Addr and alive
        user = UserTest.createUser("test3");
        service = mockService("test3", true);

        registry.associate(user, service);

        assertEquals(1, registry.getContextPreferences(monitoredAddr).size());
        assertEquals(1, registry.getContextLoggers(monitoredAddr).size());
    }

    private ResponseLoggerService mockService(String monitoredAddr, boolean alive) {
        ResponseLoggerService service = mock(ResponseLoggerService.class);
        Preferences preferences = mock(Preferences.class);

        doReturn(preferences).when(service).getPreferences();
        doReturn(monitoredAddr).when(service).getMonitoredAddr();
        doReturn(alive).when(service).isAlive();

        return service;
    }
}
