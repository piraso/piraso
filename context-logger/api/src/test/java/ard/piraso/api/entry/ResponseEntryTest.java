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

package ard.piraso.api.entry;

import ard.piraso.api.AbstractJacksonTest;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link ResponseEntry} class.
 */
public class ResponseEntryTest extends AbstractJacksonTest {
    @Test
    public void testJackson() throws IOException {
        HttpResponseEntry expected = new HttpResponseEntry();

        expected.setStatus(1);
        expected.setStatusReason("");
        expected.setElapseTime(new ElapseTimeEntry());

        expected.addCookie(createCookie());
        expected.addCookie(createCookie());

        expected.addHeader("1", 1);
        expected.addHeader("2", 2);
        expected.addHeader("3", "");
        expected.addHeader("4", "");
        expected.addHeader("5", 5l);
        expected.addHeader("6", 6l);

        String jsonValue = mapper.writeValueAsString(expected);
        HttpResponseEntry actual = mapper.readValue(jsonValue, HttpResponseEntry.class);

        assertThat("same entry", actual, is(expected));

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
