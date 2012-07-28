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

import ard.piraso.api.GeneralPreferenceEnum;
import ard.piraso.api.Level;
import ard.piraso.api.entry.MessageEntry;
import ard.piraso.api.entry.MethodCallEntry;
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
 * Test for {@link MethodCallLoggerListener} class.
 */
public class MethodCallLoggerListenerTest extends AbstractLoggerListenerTest {

    @Test
    public void testProxy() throws SQLException {
        RegexProxyFactory<Connection> factory = new RegexProxyFactory<Connection>(Connection.class);

        Connection connection = mock(Connection.class);
        ProxyInterceptorAware<Connection> proxy = factory.getProxyInterceptor(connection);

        MethodCallLoggerListener<Connection> listener = new MethodCallLoggerListener<Connection>(Level.ALL, new GroupChainId("test"));

        proxy.getInterceptor().addMethodListener("close", listener);
        proxy.getProxy().close();

        assertTrue(MethodCallEntry.class.isInstance(caughtEntry));
        verify(context, times(1)).log(any(Level.class), any(GroupChainId.class), any(MessageEntry.class));
    }

    @Test
    public void testProxyWithException() throws SQLException {
        RegexProxyFactory<Connection> factory = new RegexProxyFactory<Connection>(Connection.class);

        doReturn(true).when(context).isEnabled(GeneralPreferenceEnum.STACK_TRACE_ENABLED.getPropertyName());

        MethodCallLoggerListener<Connection> listener = new MethodCallLoggerListener<Connection>(Level.ALL, new GroupChainId("test"));
        factory.addMethodListener("close", listener);

        Connection connection = mock(Connection.class);
        ProxyInterceptorAware<Connection> proxy = factory.getProxyInterceptor(connection);

        doThrow(new SQLException()).when(connection).close();

        try {
            proxy.getProxy().close();
        } catch(Exception ignore) {}

        assertTrue(MethodCallEntry.class.isInstance(caughtEntry));
        verify(context, times(1)).log(any(Level.class), any(GroupChainId.class), any(MethodCallEntry.class));
    }
}
