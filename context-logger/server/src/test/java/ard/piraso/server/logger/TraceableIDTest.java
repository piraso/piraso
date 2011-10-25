package ard.piraso.server.logger;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 * Test for {@link TraceableID} class.
 */
public class TraceableIDTest {

    @Test
    public void testToString() throws Exception {
        TraceableID id = new TraceableID("test");

        assertEquals("[test]", id.toString());
        assertFalse(id.getIds().isEmpty());
    }

    @Test
    public void testCreateWithNoArgs() throws Exception {
        TraceableID id = new TraceableID("test");
        TraceableID id2 = id.create();
        TraceableID id3 = id2.create();

        assertEquals(1, id.getIds().size());
        assertEquals(2, id2.getIds().size());
        assertEquals(3, id3.getIds().size());

        assertEquals("test_2", id2.getIds().getLast());
        assertEquals("test_3", id3.getIds().getLast());
    }
}
