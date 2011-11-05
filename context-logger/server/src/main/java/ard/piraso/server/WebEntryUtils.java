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

package ard.piraso.server;

import ard.piraso.api.entry.CookieEntry;
import ard.piraso.api.entry.RequestEntry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;

/**
 * Coverts web objects to Entry types.
 */
public final class WebEntryUtils {

    private WebEntryUtils() {}

    public static CookieEntry toEntry(Cookie cookie) {
        CookieEntry entry = new CookieEntry();

        entry.setName(cookie.getName());
        entry.setValue(cookie.getValue());
        entry.setComment(cookie.getComment());
        entry.setDomain(cookie.getDomain());
        entry.setMaxAge(cookie.getMaxAge());
        entry.setPath(cookie.getPath());
        entry.setSecure(cookie.getSecure());
        entry.setVersion(cookie.getVersion());

        return entry;
    }

    @SuppressWarnings("unchecked")
    public static RequestEntry toEntry(HttpServletRequest request) {
        RequestEntry entry = new RequestEntry(request.getRequestURI());

        entry.setMethod(request.getMethod());
        entry.setQueryString(request.getQueryString());
        entry.setRemoteAddr(request.getRemoteAddr());
        entry.setParameters(new LinkedHashMap<String, String[]>(request.getParameterMap()));

        Enumeration<String> enumeration = request.getHeaderNames();
        while(enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            entry.addHeader(name, value);
        }

        for(Cookie cookie : request.getCookies()) {
           entry.addCookie(toEntry(cookie));
        }

        return entry;
    }
}
