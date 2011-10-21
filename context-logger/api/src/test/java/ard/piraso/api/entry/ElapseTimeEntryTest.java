package ard.piraso.api.entry;

import ard.piraso.api.AbstractJacksonTest;
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

        long elapseTime = 3000l;
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
