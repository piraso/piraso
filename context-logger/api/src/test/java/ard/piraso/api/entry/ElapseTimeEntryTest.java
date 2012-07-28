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

package ard.piraso.api.entry;

import ard.piraso.api.AbstractJacksonTest;
import ard.piraso.api.Level;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link ElapseTimeEntry} class.
 */
public class ElapseTimeEntryTest extends AbstractJacksonTest {

    @Test
    public void testPrettyPrint() throws Exception {
        testPrintElapseTime(1001 + (1000 * 60 * 60 * 24) + (1000 * 60 * 60) + (1000 * 60), "1d 1h 1m 1s 1ms");
        testPrintElapseTime(1001 + (1000 * 60 * 60) + (1000 * 60), "1h 1m 1s 1ms");
        testPrintElapseTime(1001 + (1000 * 60), "1m 1s 1ms");
        testPrintElapseTime(1001, "1s 1ms");
        testPrintElapseTime(1, "1ms");
    }

    private void testPrintElapseTime(long elapseTime, String value) {
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis() + elapseTime;

        ElapseTimeEntry entry = new ElapseTimeEntry(startTime, endTime);

        assertThat("pretty print", entry.prettyPrint(), is(value));
    }

    @Test
    public void testTimer() throws InterruptedException {
        ElapseTimeEntry entry = new ElapseTimeEntry();
        entry.start();

        // should have started
        assertTrue(entry.getStartTime() > 0);

        entry = new ElapseTimeEntry();
        entry.stop();

        // should have started and stopped
        assertTrue(entry.getStartTime() > 0);
        assertTrue(entry.getEndTime() > 0);

        long elapseTime = 1001 + (1000 * 60 * 60 * 24) + (1000 * 60 * 60) + (1000 * 60);
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis() + elapseTime;

        entry = new ElapseTimeEntry(startTime, endTime);

        // sleep for 100l milliseconds
        Thread.sleep(100l);
        // should be ignored
        entry.start();

        assertThat("same start time", entry.getStartTime(), is(startTime));
        assertThat("same end time", entry.getEndTime(), is(endTime));
        assertThat("same elapse time", entry.getElapseTime(), is(elapseTime));

        entry.restart();

        // sleep for 100l milliseconds
        Thread.sleep(100l);

        entry.stop();

        assertThat("not same start time", entry.getStartTime(), not(startTime));
        assertThat("not same end time", entry.getEndTime(), not(endTime));
        assertThat("not same elapse time", entry.getElapseTime(), not(elapseTime));
    }

    @Test
    public void testJackson() throws IOException, InterruptedException {
        long elapseTime = 3000l;
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis() + elapseTime;
        ElapseTimeEntry expected = new ElapseTimeEntry(startTime, endTime);
        expected.setLevel(Level.ALL.getName());

        String jsonValue = mapper.writeValueAsString(expected);
        ElapseTimeEntry actual = mapper.readValue(jsonValue, ElapseTimeEntry.class);

        assertThat("same start time", actual.getStartTime(), is(startTime));
        assertThat("same end time", actual.getEndTime(), is(endTime));
        assertThat("same elapse time", actual.getElapseTime(), is(elapseTime));
    }

    @Test
    public void testEquals() {
        ElapseTimeEntry entry = new ElapseTimeEntry(1l, 3l);
        ElapseTimeEntry other = new ElapseTimeEntry(2l, 4l);

        assertThat(entry.equals(entry), is(true)); // same instance
        assertThat(entry.equals("string"), is(false)); // not same class
        assertThat(entry.equals(null), is(false)); // null compare
        assertThat(entry.equals(other), is(false)); // end time not same

        other.setEndTime(3l);
        assertThat(entry.equals(other), is(false)); // start time not same

        other.setStartTime(1l);
        assertThat(entry.equals(other), is(true)); // equal
    }

    @Test
    public void testHashCode() {
        ElapseTimeEntry e1 = new ElapseTimeEntry(1l, 3l);
        ElapseTimeEntry e2 = new ElapseTimeEntry(2l, 4l);
        ElapseTimeEntry e3 = new ElapseTimeEntry(2l, 4l);

        Set<ElapseTimeEntry> set = new HashSet<ElapseTimeEntry>();
        set.add(e1);
        set.add(e2);
        set.add(e3);

        // should only be 2 since e3 and e1 is same
        assertThat(set.size(), is(2));
    }
}
