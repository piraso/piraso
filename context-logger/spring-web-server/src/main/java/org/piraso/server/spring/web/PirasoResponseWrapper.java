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

package org.piraso.server.spring.web;

import org.piraso.api.entry.HttpResponseEntry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * Responsible to collect response information.
 */
public class PirasoResponseWrapper extends HttpServletResponseWrapper {

    private HttpResponseEntry entry;

    public PirasoResponseWrapper(HttpServletResponse response, HttpResponseEntry entry) {
        super(response);

        this.entry = entry;
    }

    @Override
    public void addCookie(Cookie cookie) {
        super.addCookie(cookie);
        entry.addCookie(WebEntryUtils.toEntry(cookie));
    }

    @Override
    public void addDateHeader(String name, long date) {
        super.addDateHeader(name, date);
        entry.addHeader(name, date);
    }

    @Override
    public void addHeader(String name, String value) {
        super.addHeader(name, value);
        entry.addHeader(name, value);
    }

    @Override
    public void addIntHeader(String name, int value) {
        super.addIntHeader(name, value);
        entry.addHeader(name, value);
    }

    @Override
    public void sendError(int sc) throws IOException {
        super.sendError(sc);
        entry.setStatus(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        super.sendError(sc, msg);

        entry.setStatus(sc);
        entry.setStatusReason(msg);
    }

    @Override
    public void setDateHeader(String name, long date) {
        super.setDateHeader(name, date);
        entry.addHeader(name, date);
    }

    @Override
    public void setHeader(String name, String value) {
        super.setHeader(name, value);
        entry.addHeader(name, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
        super.setIntHeader(name, value);
        entry.addHeader(name, value);
    }

    @Override
    public void setStatus(int sc) {
        super.setStatus(sc);
        entry.setStatus(sc);
    }

    @Override
    public void setStatus(int sc, String sm) {
        super.setStatus(sc, sm);

        entry.setStatus(sc);
        entry.setStatusReason(sm);
    }
}
