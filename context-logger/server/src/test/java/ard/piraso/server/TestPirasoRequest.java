package ard.piraso.server;

import ard.piraso.api.JacksonUtils;
import ard.piraso.api.Preferences;
import org.apache.commons.lang.Validate;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static ard.piraso.api.PirasoConstants.*;

/**
 * Test piraso request.
 */
public class TestPirasoRequest implements PirasoEntryPoint, PirasoRequest {

    private MockHttpServletRequest request;

    public TestPirasoRequest(MockHttpServletRequest request) {
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
        return request.getRemoteAddr();
    }

    public String getWatchedAddr() {
        return request.getParameter(WATCHED_ADDR_PARAMETER);
    }

    public String getActivityUuid() {
        return request.getParameter(ACTIVITY_PARAMETER);
    }

    public MockHttpServletRequest getMockRequest() {
        return request;
    }
}
