package ard.piraso.server.logger;

import org.junit.Test;

import static junit.framework.Assert.*;

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

    @Test
    public void testCreateWithIntArg() throws Exception {
        TraceableID id = new TraceableID("test");
        TraceableID id2 = id.create(1);

        assertEquals(2, id2.getIds().size());
        assertEquals("[test, 1]", id2.toString());
    }

    @Test
    public void testCreateWithStringArg() throws Exception {
        TraceableID id = new TraceableID("test");
        TraceableID id2 = id.create("test2");

        assertEquals(2, id2.getIds().size());
        assertEquals("[test, test2]", id2.toString());
    }

    @Test
    public void testCreateWithStringAndHashCodeArg() throws Exception {
        TraceableID id = new TraceableID("test");
        TraceableID id2 = id.create("test2", "test2".hashCode());

        assertEquals(2, id2.getIds().size());
        assertEquals("[test, test2" + Integer.toHexString("test2".hashCode()) + "]", id2.toString());
    }

    @Test
    public void testProperty() throws Exception {
        TraceableID id = new TraceableID("test");
        id.addProperty(String.class, "hello");

        assertEquals("hello", id.getProperty(String.class));
        assertTrue(id.hasProperty(String.class));
    }
}
