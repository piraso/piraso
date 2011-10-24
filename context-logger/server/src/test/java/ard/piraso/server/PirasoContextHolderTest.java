package ard.piraso.server;

import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.mock;

/**
 * Test for {@link PirasoContextHolder} class.
 */
public class PirasoContextHolderTest {

    @Test
    public void testSetPirasoContext() throws Exception {
        PirasoContext context = mock(PirasoContext.class);

        PirasoContextHolder.setContext(context);
        assertSame(context, PirasoContextHolder.getContext());
    }

    @Test
    public void testRemoveContext() throws Exception {
        PirasoContext context = mock(PirasoContext.class);

        PirasoContextHolder.setContext(context);
        assertSame(context, PirasoContextHolder.getContext());

        PirasoContextHolder.removeContext();
        assertNull(PirasoContextHolder.getContext());
    }
}
