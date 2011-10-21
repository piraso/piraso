package ard.piraso.api.entry;

import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Test for {@link EntryUtils} class.
 */
public class EntryUtilsTest {
    @Test
    public void testToEntryNullEntries() throws Exception {
        Object[] obj = null;
        StackTraceElement[] st = null;

        assertNull(EntryUtils.toEntry(obj));
        assertNull(EntryUtils.toEntry(st));
    }

    @Test
    public void testToEntryEmpty() throws Exception {
        Object[] obj = new Object[0];
        StackTraceElement[] st = new StackTraceElement[0];

        assertTrue(EntryUtils.toEntry(obj).length == 0);
        assertTrue(EntryUtils.toEntry(st).length == 0);
    }

    @Test
    public void testToEntryMoreThanZero() throws Exception {
        Object[] obj = new Object[] {"test"};
        StackTraceElement[] st = Thread.currentThread().getStackTrace();

        assertTrue(EntryUtils.toEntry(obj).length == obj.length);
        assertTrue(EntryUtils.toEntry(st).length == st.length);
    }
}
