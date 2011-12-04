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

package ard.piraso.api.log4j;

import ard.piraso.api.AbstractJacksonTest;
import ard.piraso.api.entry.EntryUtils;
import ard.piraso.api.entry.ThrowableEntry;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link Log4jEntry} class.
 */
public class Log4jEntryTest extends AbstractJacksonTest {
    @Test
    public void testJackson() throws IOException {
        Log4jEntry expected = new Log4jEntry("INFO", "someMessage");
        expected.setStackTrace(EntryUtils.toEntry(Thread.currentThread().getStackTrace()));
        expected.setThrown(new ThrowableEntry(new Throwable()));

        String jsonValue = mapper.writeValueAsString(expected);
        Log4jEntry actual = mapper.readValue(jsonValue, Log4jEntry.class);

        assertThat("same entry", actual, is(expected));
    }
}
