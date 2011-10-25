package ard.piraso.server.dispatcher;

import ard.piraso.api.entry.MessageEntry;
import ard.piraso.server.AbstractLoggerListenerTest;
import ard.piraso.server.logger.TraceableID;
import org.junit.Test;
import org.mockito.Matchers;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test for {@link ContextLogDispatcher} class.
 */
public class ContextLogDispatcherTest extends AbstractLoggerListenerTest {

    @Test
    public void testAddRemoveListener() throws Exception {
        DispatcherForwardListener listener = mock(DispatcherForwardListener.class);

        ContextLogDispatcher.addListener(listener);

        assertEquals(1, ContextLogDispatcher.getListeners().size());

        ContextLogDispatcher.removeListener(listener);
        assertTrue(ContextLogDispatcher.getListeners().isEmpty());

        ContextLogDispatcher.addListener(listener);
        assertEquals(1, ContextLogDispatcher.getListeners().size());

        ContextLogDispatcher.clearListeners();
        assertTrue(ContextLogDispatcher.getListeners().isEmpty());
    }

    @Test
    public void testForwardWithListener() throws Exception {
        DispatcherForwardListener listener = mock(DispatcherForwardListener.class);

        ContextLogDispatcher.addListener(listener);
        ContextLogDispatcher.forward("simpleMessage");

        verify(listener, times(1)).forwarded(Matchers.<DispatcherForwardEvent>any());
        assertTrue(MessageEntry.class.isInstance(caughtEntry));
        verify(context, times(1)).log(anyString(), any(TraceableID.class), any(MessageEntry.class));
    }

    @Test
    public void testForwardWithoutListener() throws Exception {
        ContextLogDispatcher.forward("simpleMessage");

        assertTrue(MessageEntry.class.isInstance(caughtEntry));
        verify(context, times(1)).log(anyString(), any(TraceableID.class), any(MessageEntry.class));
    }

}
