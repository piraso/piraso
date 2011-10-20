package ard.piraso.api.entry;

import ard.piraso.api.AbstractJacksonTest;
import org.junit.Test;

import java.io.IOException;

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
}
