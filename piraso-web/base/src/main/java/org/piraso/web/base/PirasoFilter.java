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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.piraso.api.Level;
import org.piraso.api.entry.HttpResponseEntry;
import org.piraso.api.entry.ReferenceRequestEntry;
import org.piraso.server.GroupChainId;
import org.piraso.server.PirasoContext;
import org.piraso.server.PirasoContextHolder;
import org.piraso.server.dispatcher.ContextLogDispatcher;
import org.piraso.server.service.LoggerRegistry;
import org.piraso.server.service.LoggerRegistrySingleton;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.piraso.api.PirasoConstants.GROUP_ID_HEADER;
import static org.piraso.api.PirasoConstants.REQUEST_ID_HEADER;

/**
 * Filter responsible for adding context logging to request.
 */
public class PirasoFilter extends OncePerRequestFilter {

    /**
     * final class logging instance.
     */
    private static final Log LOG = LogFactory.getLog(PirasoFilter.class);

    public LoggerRegistry getRegistry() {
        return LoggerRegistrySingleton.INSTANCE.getRegistry();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        boolean requestIsWatched = getRegistry().isWatched(new PirasoHttpServletRequest(request));
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
                PirasoContext context = new PirasoContext(new PirasoHttpServletRequest(request), getRegistry(), ref);

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
