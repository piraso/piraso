package ard.piraso.server.spring.web;

import ard.piraso.api.JacksonUtils;
import ard.piraso.api.Preferences;
import ard.piraso.server.PirasoEntryPoint;
import ard.piraso.server.PirasoRequest;
import org.apache.commons.lang.Validate;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static ard.piraso.api.PirasoConstants.*;

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
        return request.getRemoteAddr();
    }

    public String getWatchedAddr() {
        return request.getParameter(WATCHED_ADDR_PARAMETER);
    }

    public String getActivityUuid() {
        return request.getParameter(ACTIVITY_PARAMETER);
    }
}
