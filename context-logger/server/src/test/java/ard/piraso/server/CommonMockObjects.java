package ard.piraso.server;

import ard.piraso.api.Preferences;
import ard.piraso.server.service.ResponseLoggerService;
import ard.piraso.server.service.User;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Contains helper method for mocking common objects
 */
public class CommonMockObjects {
    public static MockHttpServletRequest mockRequest(String monitoredAddr) {
        return mockRequest(monitoredAddr, null);
    }

    public static MockHttpServletRequest mockRequest(String monitoredAddr, String activityId) {
        MockHttpServletRequest request = new MockHttpServletRequest();
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
        doReturn(monitoredAddr).when(service).getMonitoredAddr();
        doReturn(alive).when(service).isAlive();

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

        return new User(request);
    }
}
