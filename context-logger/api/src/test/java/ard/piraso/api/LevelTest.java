package ard.piraso.api;

import org.junit.Test;

import java.util.HashSet;

import static junit.framework.Assert.*;

/**
 * Test for {@link Level} class.
 */
public class LevelTest {

    @Test
    public void testIsLevelAndGet() throws Exception {
        assertTrue(Level.isLevel(Level.ALL.getName()));
        assertTrue(Level.isLevel(Level.SCOPED.getName()));
        assertFalse(Level.isLevel("notALevel"));

        assertEquals(Level.ALL, Level.get(Level.ALL.getName()));
        assertNull(Level.get("NotALevel"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLevelAlreadyExists() throws Exception {
        Level.addLevel(Level.ALL.getName());
    }

    @Test
    public void testAddLevelSuccess() throws Exception {
        Level.addLevel("test");

        assertNotNull(Level.get("test"));
    }

    @Test
    public void testHashCode() throws Exception {
        HashSet<Level> levels = new HashSet<Level>();

        levels.add(Level.ALL);
        levels.add(Level.ALL);
        levels.add(Level.SCOPED);

        assertEquals(2, levels.size());
    }
}
