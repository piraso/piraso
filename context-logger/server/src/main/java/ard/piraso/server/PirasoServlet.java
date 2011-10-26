package ard.piraso.server;

import ard.piraso.server.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
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
    private static final Log LOG = LogFactory.getLog(ResponseLoggerServiceImpl.class);

    private static final String START_OPERATION = "start";

    private static final String STOP_OPERATION = "stop";

    private static final String SERVICE_PARAMETER = "service";
    public static final long STOP_TIMEOUT = 10000l;

    private Integer maxQueueForceKillSize;

    private Long maxIdleTimeout;

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

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter(SERVICE_PARAMETER) == null) {
            response.sendError(HttpStatus.BAD_REQUEST.ordinal(), "Request Parameter 'service' is required.");
            return;
        }

        User user = registry.createOrGetUser(request);

        if(START_OPERATION.equals(request.getParameter(SERVICE_PARAMETER))) {
            startLoggerService(request, response, user);
        } else if(STOP_OPERATION.equals(request.getParameter(SERVICE_PARAMETER))) {
            stopService(response, user);
        } else {
            response.sendError(HttpStatus.BAD_REQUEST.value(),
                    String.format("Request Parameter 'service' with value '%s' is invalid.",
                            request.getParameter(SERVICE_PARAMETER)));
        }
    }

    private void stopService(HttpServletResponse response, User user) throws IOException {
        ResponseLoggerService service = registry.getLogger(user);

        if(service == null) {
            response.sendError(HttpStatus.NOT_FOUND.value(), String.format("Service for user '%s' not found.", user.toString()));
            return;
        }

        if(!service.isAlive()) {
            response.sendError(HttpStatus.CONFLICT.value(), String.format("Service for user '%s' not active.", user.toString()));
            registry.removeUser(user);

            return;
        }

        try {
            service.stopAndWait(STOP_TIMEOUT);

            if(service.isAlive()) {
                response.sendError(HttpStatus.REQUEST_TIMEOUT.value(), String.format("Service for user '%s' stop timeout.", user.toString()));
                return;
            } else {
                registry.removeUser(user);
            }
        } catch (InterruptedException ignored) {
        }
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
        } catch (ForcedStoppedException e) {
            LOG.warn(String.format("User '%s' logging service was forced stopped.", user.toString()), e);
            registry.removeUser(user);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);

            throw new ServletException(e.getMessage(), e);
        }
    }
}
