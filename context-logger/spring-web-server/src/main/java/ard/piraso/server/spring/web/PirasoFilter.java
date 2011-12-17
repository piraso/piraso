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

package ard.piraso.server.spring.web;

import ard.piraso.api.Level;
import ard.piraso.api.entry.HttpResponseEntry;
import ard.piraso.api.entry.ReferenceRequestEntry;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.PirasoContext;
import ard.piraso.server.PirasoContextHolder;
import ard.piraso.server.dispatcher.ContextLogDispatcher;
import ard.piraso.server.service.UserRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ard.piraso.api.PirasoConstants.GROUP_ID_HEADER;
import static ard.piraso.api.PirasoConstants.REQUEST_ID_HEADER;

/**
 * Filter responsible for adding context logging to request.
 */
public class PirasoFilter extends OncePerRequestFilter {

    /**
     * final class logging instance.
     */
    private static final Log LOG = LogFactory.getLog(PirasoFilter.class);

    private UserRegistry registry;

    public void setRegistry(UserRegistry registry) {
        this.registry = registry;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        boolean requestIsWatched = registry.isWatched(new PirasoHttpServletRequest(request));
        HttpResponseEntry responseEntry = new HttpResponseEntry();

        try {
            if(requestIsWatched) {
                responseEntry.getElapseTime().start();

                ReferenceRequestEntry ref = null;
                try {
                    if(request.getHeader(REQUEST_ID_HEADER) != null && request.getHeader(GROUP_ID_HEADER) != null) {
                        ref = new ReferenceRequestEntry();

                        ref.setRemoteAddress(request.getRemoteAddr());
                        ref.setRequestId(Long.valueOf(request.getHeader(REQUEST_ID_HEADER)));
                        ref.setGroupId(request.getHeader(GROUP_ID_HEADER));
                        ref.setServerName(request.getRemoteHost());
                    }
                } catch(RuntimeException e) {
                    ref = null;
                }

                response = new PirasoResponseWrapper(response, responseEntry);
                PirasoContext context = new PirasoContext(new PirasoHttpServletRequest(request), registry, ref);

                PirasoContextHolder.setContext(context);

                // forward a scoped context log for request entry point
                ContextLogDispatcher.forward(Level.SCOPED, new GroupChainId("request", request.hashCode()),
                        WebEntryUtils.toEntry(request));
            }
        } catch(Exception e) {
            LOG.warn(e.getMessage(), e);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            if(requestIsWatched) {
                responseEntry.getElapseTime().stop();

                // forward a scoped context log for response exit point
                ContextLogDispatcher.forward(Level.SCOPED, new GroupChainId("response", request.hashCode()),
                        responseEntry);

                PirasoContextHolder.removeContext();
            }
        }
    }
}
