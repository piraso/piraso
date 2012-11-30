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

import org.piraso.api.AbstractJacksonTest;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedHashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link RequestEntry} class.
 */
public class HttpRequestEntryTest extends AbstractJacksonTest {
    @Test
    public void testJackson() throws IOException {
        HttpRequestEntry expected = new HttpRequestEntry("/");
        expected.setMethod("GET");
        expected.setParameters(new LinkedHashMap<String, String[]>());
        expected.setQueryString("test=1");
        expected.setRemoteAddr("127.0.0.1");

        expected.addCookie(createCookie());
        expected.addCookie(createCookie());
        expected.addHeader("test", "test");
        expected.addHeader("test2", "test");

        String jsonValue = mapper.writeValueAsString(expected);
        HttpRequestEntry actual = mapper.readValue(jsonValue, HttpRequestEntry.class);

        assertThat("same entry", actual, is(expected));
        assertTrue(actual.toString().contains(expected.getUri()));
    }

    private CookieEntry createCookie() {
        CookieEntry entry = new CookieEntry();
        entry.setMaxAge(1);
        entry.setPath("/");
        entry.setSecure(true);
        entry.setVersion(1);
        entry.setComment("comment");
        entry.setDomain("domain");

        return entry;
    }

}
