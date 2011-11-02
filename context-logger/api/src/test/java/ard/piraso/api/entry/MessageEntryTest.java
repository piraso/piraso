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
