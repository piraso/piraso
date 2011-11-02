package ard.piraso.server.logger;

import ard.piraso.server.GroupChainId;
import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Test for {@link ard.piraso.server.GroupChainId} class.
 */
public class GroupChainIdTest {

    @Test
    public void testToString() throws Exception {
        GroupChainId id = new GroupChainId("test");

        assertEquals("[test]", id.toString());
        assertFalse(id.getGroupIds().isEmpty());
    }

    @Test
    public void testCreateWithNoArgs() throws Exception {
        GroupChainId id = new GroupChainId("test");
        GroupChainId id2 = id.create();
        GroupChainId id3 = id2.create();

        assertEquals(1, id.getGroupIds().size());
        assertEquals(2, id2.getGroupIds().size());
        assertEquals(3, id3.getGroupIds().size());

        assertEquals("test_2", id2.getGroupIds().getLast());
        assertEquals("test_3", id3.getGroupIds().getLast());
    }

    @Test
    public void testCreateWithIntArg() throws Exception {
        GroupChainId id = new GroupChainId("test");
        GroupChainId id2 = id.create(1);

        assertEquals(2, id2.getGroupIds().size());
        assertEquals("[test, 1]", id2.toString());
    }

    @Test
    public void testCreateWithStringAndHashCode() throws Exception {
        GroupChainId id = new GroupChainId("test", "test".hashCode());

        assertEquals(1, id.getGroupIds().size());
    }

    @Test
    public void testCreateWithStringArg() throws Exception {
        GroupChainId id = new GroupChainId("test");
        GroupChainId id2 = id.create("test2");

        assertEquals(2, id2.getGroupIds().size());
        assertEquals("[test, test2]", id2.toString());
    }

    @Test
    public void testCreateWithStringAndHashCodeArg() throws Exception {
        GroupChainId id = new GroupChainId("test");
        GroupChainId id2 = id.create("test2", "test2".hashCode());

        assertEquals(2, id2.getGroupIds().size());
        assertEquals("[test, test2" + Integer.toHexString("test2".hashCode()) + "]", id2.toString());
    }

    @Test
    public void testProperty() throws Exception {
        GroupChainId id = new GroupChainId("test");
        id.addProperty(String.class, "hello");

        assertEquals("hello", id.getProperty(String.class));
        assertTrue(id.hasProperty(String.class));
    }
}
