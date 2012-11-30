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

import java.util.*;

/**
 * Http response entry
 */
public class HttpResponseEntry extends ResponseEntry {

    private Map<String, HeaderEntry<Integer>> intHeader;

    private Map<String, HeaderEntry<String>> stringHeader;

    private Map<String, HeaderEntry<Long>> dateHeader;

    private List<CookieEntry> cookies;

    public HttpResponseEntry() {}

    public void addCookie(CookieEntry cookie) {
        if(cookies == null) {
            cookies = new ArrayList<CookieEntry>();
        }

        this.cookies.add(cookie);
    }

    public void addHeader(String name, Integer value) {
        if(intHeader == null) {
            intHeader = new LinkedHashMap<String, HeaderEntry<Integer>>();
        }

        intHeader.put(name, new HeaderEntry<Integer>(name, value));
    }

    public void addHeader(String name, String value) {
        if(stringHeader == null) {
            stringHeader = new LinkedHashMap<String, HeaderEntry<String>>();
        }

        stringHeader.put(name, new HeaderEntry<String>(name, value));
    }

    public void addHeader(String name, Long value) {
        if(dateHeader == null) {
            dateHeader = new LinkedHashMap<String, HeaderEntry<Long>>();
        }

        dateHeader.put(name, new HeaderEntry<Long>(name, value));
    }

    public List<CookieEntry> getCookies() {
        return cookies;
    }

    public void setCookies(LinkedList<CookieEntry> cookies) {
        this.cookies = cookies;
    }

    public Map<String, HeaderEntry<Integer>> getIntHeader() {
        return intHeader;
    }

    public void setIntHeader(LinkedHashMap<String, HeaderEntry<Integer>> intHeader) {
        this.intHeader = intHeader;
    }

    public Map<String, HeaderEntry<String>> getStringHeader() {
        return stringHeader;
    }

    public void setStringHeader(LinkedHashMap<String, HeaderEntry<String>> stringHeader) {
        this.stringHeader = stringHeader;
    }

    public Map<String, HeaderEntry<Long>> getDateHeader() {
        return dateHeader;
    }

    public void setDateHeader(LinkedHashMap<String, HeaderEntry<Long>> dateHeader) {
        this.dateHeader = dateHeader;
    }
}
