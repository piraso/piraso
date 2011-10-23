package ard.piraso.api;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Test for {@link IDGenerator}.
 */
public class IDGeneratorTest {

    @Test
    public void testNext() throws Exception {
        IDGenerator generator = new IDGenerator();

        assertEquals(1l, generator.next());
        assertEquals(2l, generator.next());
        assertEquals(3l, generator.next());
    }
}
