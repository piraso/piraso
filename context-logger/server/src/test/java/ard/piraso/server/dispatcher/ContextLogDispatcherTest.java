package ard.piraso.server.dispatcher;

import ard.piraso.api.Level;
import ard.piraso.api.entry.MessageEntry;
import ard.piraso.server.AbstractLoggerListenerTest;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.PirasoContextHolder;
import org.junit.Test;
import org.mockito.Matchers;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.any;
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
    public void testForwardNoContext() throws Exception {
        PirasoContextHolder.removeContext();
        DispatcherForwardListener listener = spy(new DispatcherHandler());

        ContextLogDispatcher.addListener(listener);
        ContextLogDispatcher.forward("simpleMessage");

        verify(listener, times(0)).forwarded(Matchers.<DispatcherForwardEvent>any());
        verify(context, times(0)).log(Matchers.<Level>any(), any(GroupChainId.class), any(MessageEntry.class));
    }

    @Test
    public void testForwardWithListener() throws Exception {
        DispatcherForwardListener listener = spy(new DispatcherHandler());

        ContextLogDispatcher.addListener(listener);
        ContextLogDispatcher.forward("simpleMessage");

        verify(listener, times(1)).forwarded(Matchers.<DispatcherForwardEvent>any());
        assertTrue(MessageEntry.class.isInstance(caughtEntry));
        verify(context, times(1)).log(Matchers.<Level>any(), any(GroupChainId.class), any(MessageEntry.class));
    }

    @Test
    public void testForwardWithoutListener() throws Exception {
        ContextLogDispatcher.forward("simpleMessage");

        assertTrue(MessageEntry.class.isInstance(caughtEntry));
        verify(context, times(1)).log(Matchers.<Level>any(), any(GroupChainId.class), any(MessageEntry.class));
    }

    private class DispatcherHandler implements DispatcherForwardListener {

        public void forwarded(DispatcherForwardEvent evt) {
            assertNotNull(evt.getEntry());
            assertNotNull(evt.getId());
        }
    }
}
