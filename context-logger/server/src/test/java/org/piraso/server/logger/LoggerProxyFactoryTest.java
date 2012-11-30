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

package org.piraso.server.logger;

import org.piraso.proxy.RegexMethodInterceptorAdapter;
import org.piraso.proxy.RegexMethodInterceptorEvent;
import org.piraso.proxy.RegexProxyFactory;
import org.piraso.server.GeneralPreferenceEvaluator;
import org.piraso.server.GroupChainId;
import org.piraso.server.PirasoContext;
import org.piraso.server.PirasoContextHolder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

/**
 * Test for {@link AbstractLoggerProxyFactory} class.
 */
public class LoggerProxyFactoryTest {

    protected PirasoContext context;

    protected GroupChainId id;

    @Before
    public void setUp() throws Exception {
        id = new GroupChainId("test");
        context = mock(PirasoContext.class);

        PirasoContextHolder.setContext(context);
    }

    @Test
    public void testProxy() throws SQLException {
        RegexProxyFactory<Connection> rf = new RegexProxyFactory<Connection>(Connection.class);
        LoggerProxyFactory<Connection> factory = new LoggerProxyFactory<Connection>(id, rf);

        RegexMethodInterceptorAdapter<Connection> adapter = spy(new RegexMethodInterceptorAdapter<Connection>());

        rf.addMethodListener("close", adapter);

        Connection connection = mock(Connection.class);
        Connection proxy = factory.getProxy(connection);

        proxy.commit();

        verify(adapter, times(0)).beforeCall(Matchers.<RegexMethodInterceptorEvent<Connection>>any());
        verify(adapter, times(0)).afterCall(Matchers.<RegexMethodInterceptorEvent<Connection>>any());
        verify(adapter, times(0)).exceptionCall(Matchers.<RegexMethodInterceptorEvent<Connection>>any());

        proxy.close();

        verify(adapter, times(1)).beforeCall(Matchers.<RegexMethodInterceptorEvent<Connection>>any());
        verify(adapter, times(1)).afterCall(Matchers.<RegexMethodInterceptorEvent<Connection>>any());
        verify(adapter, times(0)).exceptionCall(Matchers.<RegexMethodInterceptorEvent<Connection>>any());

    }

    @Test
    public void testProxyWithException() throws SQLException {
        RegexProxyFactory<Connection> rf = new RegexProxyFactory<Connection>(Connection.class);
        LoggerProxyFactory<Connection> factory = new LoggerProxyFactory<Connection>(id, rf);

        RegexMethodInterceptorAdapter<Connection> adapter = spy(new RegexMethodInterceptorAdapter<Connection>());
        RegexMethodInterceptorAdapter<Connection> adapter2 = spy(new RegexMethodInterceptorAdapter<Connection>());


        rf.addMethodListener("close", adapter);
        rf.addMethodListener("close", adapter2);

        Connection connection = mock(Connection.class);
        Connection proxy = factory.getProxy(connection);

        doThrow(new SQLException()).when(connection).close();

        try {
            proxy.close();
        } catch(Exception ignore) {}

        verify(adapter, times(1)).beforeCall(Matchers.<RegexMethodInterceptorEvent<Connection>>any());
        verify(adapter, times(0)).afterCall(Matchers.<RegexMethodInterceptorEvent<Connection>>any());
        verify(adapter, times(1)).exceptionCall(Matchers.<RegexMethodInterceptorEvent<Connection>>any());
        verify(adapter2, times(1)).beforeCall(Matchers.<RegexMethodInterceptorEvent<Connection>>any());
        verify(adapter2, times(0)).afterCall(Matchers.<RegexMethodInterceptorEvent<Connection>>any());
        verify(adapter2, times(1)).exceptionCall(Matchers.<RegexMethodInterceptorEvent<Connection>>any());
    }

    protected  class LoggerProxyFactory<T> extends AbstractLoggerProxyFactory<T, GeneralPreferenceEvaluator> {
        public LoggerProxyFactory(GroupChainId id, RegexProxyFactory<T> regexProxyFactory) {
            super(id, regexProxyFactory, new GeneralPreferenceEvaluator());
        }
    }
}
