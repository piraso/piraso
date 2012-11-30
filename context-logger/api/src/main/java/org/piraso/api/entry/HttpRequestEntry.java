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

package org.piraso.api.entry;

import org.piraso.api.PirasoConstants;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Http request entry.
 */
public class HttpRequestEntry extends RequestEntry {
    private String uri;

    private String queryString;

    private String method;

    private String remoteAddr;

    private Map<String, String> headers;

    private Map<String, String[]> parameters;

    private List<CookieEntry> cookies;

    public HttpRequestEntry() {}

    public HttpRequestEntry(String uri) {
        super(uri);
        this.uri = uri;
    }

    public void addHeader(String name, String value) {
        if(headers == null) {
            headers = new LinkedHashMap<String, String>();
        }

        headers.put(name, value);
    }

    public void addCookie(CookieEntry cookieEntry) {
        if(cookies == null) {
            cookies = new ArrayList<CookieEntry>();
        }

        cookies.add(cookieEntry);
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(LinkedHashMap<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String[]> getParameters() {
        return parameters;
    }

    public void setParameters(LinkedHashMap<String, String[]> parameters) {
        this.parameters = parameters;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<CookieEntry> getCookies() {
        return cookies;
    }

    public void setCookies(List<CookieEntry> cookies) {
        this.cookies = cookies;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(super.toString());

        if(StringUtils.isNotEmpty(getQueryString())) {
            buf.append("?");
            buf.append(getQueryString());
        }

        if(MapUtils.isNotEmpty(headers)) {
            for(Map.Entry<String, String> entry : headers.entrySet()) {
                if(PirasoConstants.METHOD_NAME_HEADER.equalsIgnoreCase(entry.getKey())) {
                    buf.append("?methodName=");
                    buf.append(entry.getValue());
                }
            }
        }

        return buf.toString();
    }
}
