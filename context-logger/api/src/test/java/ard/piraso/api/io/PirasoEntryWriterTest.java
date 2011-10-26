package ard.piraso.api.io;

import ard.piraso.api.entry.MessageEntry;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertTrue;

/**
 * Test for {@link PirasoEntryWriter} classes.
 */
public class PirasoEntryWriterTest {

    @Test
    public void testWrite() throws Exception {
        StringWriter buf = new StringWriter();

        PirasoEntryWriter writer = new PirasoEntryWriter("1", "2", new PrintWriter(buf));
        writer.write("id_1", new MessageEntry("message"));
        writer.close();

        String actual = buf.toString();

        assertTrue(actual.contains("id=\"1\""));
        assertTrue(actual.contains("monitor=\"2\""));
        assertTrue(actual.contains("id_1"));
        assertTrue(actual.contains("message"));
        assertTrue(actual.contains(MessageEntry.class.getName()));
    }
}
