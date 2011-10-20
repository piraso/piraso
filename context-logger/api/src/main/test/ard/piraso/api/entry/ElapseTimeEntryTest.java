package ard.piraso.api.entry;

import ard.piraso.api.AbstractJacksonTest;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link ElapseTimeEntry} class.
 */
public class ElapseTimeEntryTest extends AbstractJacksonTest {

    @Test
    public void testStartAndReset() throws InterruptedException {
        long elapseTime = 3000l;
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis() + elapseTime;

        ElapseTimeEntry entry = new ElapseTimeEntry(startTime, endTime);

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
}
