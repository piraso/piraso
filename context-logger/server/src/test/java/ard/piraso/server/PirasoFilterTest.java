package ard.piraso.server;

import ard.piraso.api.entry.RequestEntry;
import ard.piraso.server.dispatcher.ContextLogDispatcher;
import ard.piraso.server.dispatcher.DispatcherForwardEvent;
import ard.piraso.server.dispatcher.DispatcherForwardListener;
import ard.piraso.server.service.UserRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;

import static ard.piraso.server.CommonMockObjects.mockRequest;
import static org.mockito.Mockito.*;

/**
 * Test for {@link PirasoFilter} class.
 */
public class PirasoFilterTest {

    public static final String MONITORED_ADDR = "127.0.0.1";

    private UserRegistry registry;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    private MockFilterChain chain;

    private PirasoFilter filter;

    @Before
    public void setUp() throws Exception {
        registry = spy(new UserRegistry());
        request = mockRequest(MONITORED_ADDR);
        response = spy(new MockHttpServletResponse());
        chain = spy(new MockFilterChain());

        request.setCookies(new Cookie("name", "value"));
        request.addParameter("name", "value");
        request.addHeader("name", "value");

        filter = new PirasoFilter();
        filter.setRegistry(registry);
    }

    @After
    public void tearDown() throws Exception {
        ContextLogDispatcher.clearListeners();
    }

    @Test
    public void testNotWatched() throws Exception {
        doReturn(false).when(registry).isWatched(request);

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    public void testWatched() throws Exception {
        doReturn(true).when(registry).isWatched(request);

        filter.doFilterInternal(request, response, chain);

        // since response is wraped
        // the first should not be invoked once
        // but the second should be invoked at least once.
        verify(chain, times(0)).doFilter(request, response);
        verify(chain, times(1)).doFilter(Matchers.<ServletRequest>any(), Matchers.<ServletResponse>any());
    }

    @Test
    public void testWatchedWithException() throws Exception {
        doReturn(true).when(registry).isWatched(request);

        // always thrown an exception
        // which should always be ignored.
        ContextLogDispatcher.addListener(new DispatcherForwardListener() {
            public void forwarded(DispatcherForwardEvent evt) {
                if(evt.getEntry() instanceof RequestEntry) {
                    throw new IllegalStateException();
                }
            }
        });

        filter.doFilterInternal(request, response, chain);

        // since response is wraped
        // the first should not be invoked once
        // but the second should be invoked at least once.
        verify(chain, times(0)).doFilter(request, response);
        verify(chain, times(1)).doFilter(Matchers.<ServletRequest>any(), Matchers.<ServletResponse>any());
    }
}
