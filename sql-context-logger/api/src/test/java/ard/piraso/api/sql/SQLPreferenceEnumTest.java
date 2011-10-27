package ard.piraso.api.sql;

import ard.piraso.api.Level;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Test for {@link SQLPreferenceEnum} enum.
 */
public class SQLPreferenceEnumTest {
    @Test
    public void testLevels() throws Exception {
        for(SQLPreferenceEnum flag : SQLPreferenceEnum.values()) {
            if(flag.isLevel()) {
                assertTrue(Level.isLevel(flag.getPropertyName()));
            }
        }
    }
}
