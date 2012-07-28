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

package ard.piraso.server.logger;

import ard.piraso.api.Level;
import ard.piraso.api.entry.ElapseTimeEntry;
import ard.piraso.api.entry.MessageEntry;
import ard.piraso.proxy.ProxyInterceptorAware;
import ard.piraso.proxy.RegexProxyFactory;
import ard.piraso.server.AbstractLoggerListenerTest;
import ard.piraso.server.GroupChainId;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Test for {@link MessageLoggerListener} class.
 */
public class MessageLoggerListenerTest extends AbstractLoggerListenerTest {

    @Test
    public void testProxy() throws SQLException {
        RegexProxyFactory<Connection> factory = new RegexProxyFactory<Connection>(Connection.class);

        MessageLoggerListener<Connection> listener = new MessageLoggerListener<Connection>(null, new GroupChainId("test"), "test");
        factory.addMethodListener("close", listener);

        Connection connection = mock(Connection.class);
        ProxyInterceptorAware<Connection> proxy = factory.getProxyInterceptor(connection);

        proxy.getProxy().close();

        assertTrue(MessageEntry.class.isInstance(caughtEntry));
        verify(context, times(1)).log(any(Level.class), any(GroupChainId.class), any(MessageEntry.class));
    }

    @Test
    public void testProxyWithException() throws SQLException {
        RegexProxyFactory<Connection> rf = new RegexProxyFactory<Connection>(Connection.class);

        MessageLoggerListener<Connection> listener = new MessageLoggerListener<Connection>(null, new GroupChainId("test"), "test");
        rf.addMethodListener("close", listener);

        Connection connection = mock(Connection.class);
        ProxyInterceptorAware<Connection> proxy = rf.getProxyInterceptor(connection);

        doThrow(new SQLException()).when(connection).close();

        try {
            proxy.getProxy().close();
        } catch(Exception ignore) {}

        assertTrue(MessageEntry.class.isInstance(caughtEntry));
        verify(context, times(1)).log(any(Level.class), any(GroupChainId.class), any(MessageEntry.class));
    }

    @Test
    public void testProxyWithElapseTime() throws SQLException {
        RegexProxyFactory<Connection> factory = new RegexProxyFactory<Connection>(Connection.class);

        Connection connection = mock(Connection.class);
        ProxyInterceptorAware<Connection> proxy = factory.getProxyInterceptor(connection);

        ElapseTimeEntry elapseTime = new ElapseTimeEntry();
        elapseTime.start();
        MessageLoggerListener<Connection> listener = new MessageLoggerListener<Connection>(null, new GroupChainId("test"), "test", elapseTime);

        proxy.getInterceptor().addMethodListener("close", listener);
        proxy.getProxy().close();

        assertTrue(MessageEntry.class.isInstance(caughtEntry));
        verify(context, times(1)).log(any(Level.class), any(GroupChainId.class), any(MessageEntry.class));
    }
}
