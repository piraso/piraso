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

package org.piraso.web.base;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.piraso.api.entry.RequestEntry;
import org.piraso.server.CommonMockObjects;
import org.piraso.server.PirasoEntryPoint;
import org.piraso.server.dispatcher.ContextLogDispatcher;
import org.piraso.server.dispatcher.DispatcherForwardEvent;
import org.piraso.server.dispatcher.DispatcherForwardListener;
import org.piraso.server.service.DefaultUserRegistryImpl;
import org.piraso.server.service.LoggerRegistrySingleton;
import org.piraso.server.service.UserRegistry;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;

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
        registry = spy(new DefaultUserRegistryImpl());
        request = CommonMockObjects.mockRequest(MONITORED_ADDR);
        response = spy(new MockHttpServletResponse());
        chain = spy(new MockFilterChain());

        request.setCookies(new Cookie("name", "value"));
        request.addParameter("name", "value");
        request.addHeader("name", "value");

        filter = new PirasoFilter();
        LoggerRegistrySingleton.INSTANCE.setRegistry(registry);
    }

    @After
    public void tearDown() throws Exception {
        ContextLogDispatcher.clearListeners();
    }

    @Test
    public void testNotWatched() throws Exception {
        doReturn(false).when(registry).isWatched(Matchers.<PirasoEntryPoint>any());

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    public void testWatched() throws Exception {
        doReturn(true).when(registry).isWatched(Matchers.<PirasoEntryPoint>any());

        filter.doFilterInternal(request, response, chain);

        // since response is wraped
        // the first should not be invoked once
        // but the second should be invoked at least once.
        verify(chain, times(0)).doFilter(request, response);
        verify(chain, times(1)).doFilter(Matchers.<ServletRequest>any(), Matchers.<ServletResponse>any());
    }

    @Test
    public void testWatchedWithException() throws Exception {
        doReturn(true).when(registry).isWatched(Matchers.<PirasoEntryPoint>any());

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
