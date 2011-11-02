package ard.piraso.server.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static ard.piraso.server.CommonMockObjects.*;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        User expected = createUser("test", "a1");
        User actual = registry.createOrGetUser(mockRequest("test", "a1"));

        assertEquals(expected, actual);
    }

    @Test
    public void testAssociate() throws Exception {
        MockHttpServletRequest request = mockRequest("test", "a1");

        User user = registry.createOrGetUser(request);
        ResponseLoggerService service = mockService(request.getRemoteAddr(), true);

        registry.associate(user, service);

        assertEquals(1, registry.getContextPreferences(request).size());
        assertEquals(1, registry.getContextLoggers(request).size());
        assertTrue(registry.isUserExist(user));
    }

    @Test
    public void testIsWatched() throws Exception {
        MockHttpServletRequest request = mockRequest("test", "a1");

        assertFalse(registry.isWatched(request));

        User user = registry.createOrGetUser(request);
        ResponseLoggerService service = mockService(request.getRemoteAddr(), true);

        registry.associate(user, service);

        assertTrue(registry.isWatched(request));
    }

    @Test
    public void testRemove() throws Exception {
        MockHttpServletRequest request = mockRequest("test", "a1");

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
        MockHttpServletRequest request = mockRequest("test", "a1");
        MockHttpServletRequest orig = request;

        User user = registry.createOrGetUser(request);
        ResponseLoggerService service = mockService(request.getRemoteAddr(), true);

        registry.associate(user, service);

        assertEquals(1, registry.getContextPreferences(orig).size());
        assertEquals(1, registry.getContextLoggers(orig).size());

        // not valid url
        service.getPreferences().addUrlPattern("/someValidUrl");
        request.setRequestURI("/notValid");

        assertEquals(0, registry.getContextPreferences(orig).size());
        assertEquals(0, registry.getContextLoggers(orig).size());

        // remove patterns
        service.getPreferences().setUrlPatterns(null);

        // not alive
        request = mockRequest("test2");
        User user2 = registry.createOrGetUser(request);
        ResponseLoggerService service2 = mockService(request.getRemoteAddr(), false);

        registry.associate(user2, service2);

        assertEquals(1, registry.getContextPreferences(orig).size());
        assertEquals(1, registry.getContextLoggers(orig).size());

        // not same monitored Addr and alive
        request = mockRequest("test3");
        User user3 = registry.createOrGetUser(request);
        ResponseLoggerService service3 = mockService(request.getRemoteAddr(), true);

        registry.associate(user3, service3);

        assertEquals(1, registry.getContextPreferences(orig).size());
        assertEquals(1, registry.getContextLoggers(orig).size());
    }
}
