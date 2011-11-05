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
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link MessageEntry} class.
 */
public class MessageEntryTest extends AbstractJacksonTest {
    @Test
    public void testJackson() throws IOException {
        long elapseTime = 3000l;
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis() + elapseTime;

        ElapseTimeEntry expectedElapseTime = new ElapseTimeEntry(startTime, endTime);
        MessageEntry expectedMessage = new MessageEntry("message");
        expectedMessage.setElapseTime(expectedElapseTime);

        String jsonValue = mapper.writeValueAsString(expectedMessage);
        MessageEntry actualMessage = mapper.readValue(jsonValue, MessageEntry.class);
        ElapseTimeEntry actualElapseTime = actualMessage.getElapseTime();

        assertThat("same entry", actualMessage, is(expectedMessage));
        assertThat("same elapse time", actualElapseTime.getElapseTime(), is(elapseTime));
    }

    @Test
    public void testEquals() {
        MessageEntry entry = new MessageEntry(1l, "message");
        MessageEntry other = new MessageEntry(1l, "not same");

        assertThat(entry.equals(entry), is(true)); // same instance
        assertThat(entry.equals("string"), is(false)); // not same class
        assertThat(entry.equals(null), is(false)); // null compare

        assertThat(entry.equals(other), is(false)); // elapse time not same

        entry.setElapseTime(new ElapseTimeEntry());
        assertThat(entry.equals(other), is(false)); // elapse time not same

        other.setElapseTime(new ElapseTimeEntry());
        assertThat(entry.equals(other), is(false)); // message not same
     }

    @Test
    public void testHashCode() {
        MessageEntry e1 = new MessageEntry(1l, "test");
        MessageEntry e2 = new MessageEntry(1l, "test2") {{
            setElapseTime(new ElapseTimeEntry());
        }};

        MessageEntry e3 = new MessageEntry(1l, "test");
        MessageEntry e4 = new MessageEntry("test", new ElapseTimeEntry());

        Set<MessageEntry> set = new HashSet<MessageEntry>();
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);

        // should only be 2 since e3 and e1 is same
        assertThat(set.size(), is(3));
    }
}
