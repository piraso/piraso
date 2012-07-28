/*
 * Copyright (c) 2012. Piraso Alvin R. de Leon. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The Piraso licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
