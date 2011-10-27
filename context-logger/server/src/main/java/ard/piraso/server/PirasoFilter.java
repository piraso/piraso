package ard.piraso.server;

import ard.piraso.api.GeneralPreferenceEnum;
import ard.piraso.api.entry.ResponseEntry;
import ard.piraso.server.dispatcher.ContextLogDispatcher;
import ard.piraso.server.logger.TraceableID;
import ard.piraso.server.service.UserRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter responsible for adding context logging to request.
 */
public class PirasoFilter extends OncePerRequestFilter {

    /**
     * final class logging instance.
     */
    private static final Log LOG = LogFactory.getLog(PirasoFilter.class);

    private static final String SCOPED_LOG = GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName();

    private UserRegistry registry;

    public void setRegistry(UserRegistry registry) {
        this.registry = registry;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        boolean requestIsWatched = registry.isWatched(request);
        ResponseEntry responseEntry = null;

        try {
            if(requestIsWatched) {
                responseEntry = new ResponseEntry();

                responseEntry.getElapseTime().start();

                response = new PirasoResponseWrapper(response, responseEntry);
                PirasoContext context = new PirasoContext(request, registry);

                PirasoContextHolder.setContext(context);

                // forward a scoped context log for request entry point
                ContextLogDispatcher.forward(SCOPED_LOG, new TraceableID("request-" + request.hashCode()),
                        WebEntryUtils.toEntry(request));
            }
        } catch(Exception e) {
            LOG.warn(e.getMessage(), e);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            if(requestIsWatched && responseEntry != null) {
                responseEntry.getElapseTime().stop();

                // forward a scoped context log for response exit point
                ContextLogDispatcher.forward(SCOPED_LOG, new TraceableID("response-" + request.hashCode()),
                        responseEntry);

                PirasoContextHolder.removeContext();
            }
        }
    }
}
