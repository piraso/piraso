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

package org.piraso.proxy;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.mockito.Matchers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test for {@link RegexProxyFactory} class.
 */
public class RegexProxyFactoryTest {

    @Test
    public void testGetProxy() throws Exception {
        DataSource actual = mock(DataSource.class);
        RegexProxyFactory<DataSource> factory = new RegexProxyFactory<DataSource>(DataSource.class);

        DataSource proxy = factory.getProxy(actual);

        // invoked twice
        proxy.getConnection();
        proxy.getConnection();

        assertThat(proxy, not(sameInstance(actual)));
        verify(actual, times(2)).getConnection();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalListener() throws Exception {
        RegexProxyFactory<DataSource> factory = new RegexProxyFactory<DataSource>(DataSource.class);

        factory.addMethodListener(".*", null);
    }

    @Test
    public void testGetProxyInterceptor() throws Exception {
        DataSource actual = mock(DataSource.class);
        RegexProxyFactory<DataSource> factory = new RegexProxyFactory<DataSource>(DataSource.class);

        ProxyInterceptorAware<DataSource> proxy = factory.getProxyInterceptor(actual);

        // invoked twice
        proxy.getProxy().getConnection();
        proxy.getProxy().getConnection();

        assertNotNull(proxy.getInterceptor());
        assertThat(proxy.getProxy(), not(sameInstance(actual)));
        verify(actual, times(2)).getConnection();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testErrorMethodListener() throws Exception {
        final DataSource actual = mock(DataSource.class);
        doReturn(mock(Connection.class)).when(actual).getConnection();

        RegexMethodInterceptorAdapter<DataSource> adapter = mock(RegexMethodInterceptorAdapter.class);

        doThrow(new IllegalArgumentException()).when(adapter).afterCall(Matchers.<RegexMethodInterceptorEvent<DataSource>>any());
        doThrow(new IllegalArgumentException()).when(adapter).beforeCall(Matchers.<RegexMethodInterceptorEvent<DataSource>>any());
        doThrow(new IllegalArgumentException()).when(adapter).exceptionCall(Matchers.<RegexMethodInterceptorEvent<DataSource>>any());

        final RegexProxyFactory<DataSource> factory = new RegexProxyFactory<DataSource>(DataSource.class);
        factory.addMethodListener("getConnection", adapter);

        DataSource proxy = factory.getProxy(actual);

        proxy.getConnection();

        verify(adapter).afterCall(Matchers.<RegexMethodInterceptorEvent<DataSource>>any());
        verify(adapter).beforeCall(Matchers.<RegexMethodInterceptorEvent<DataSource>>any());
        verify(adapter, times(0)).exceptionCall(Matchers.<RegexMethodInterceptorEvent<DataSource>>any());


        // throw exception when connection is invoked
        doThrow(new IllegalStateException()).when(actual).getConnection();

        // invoked to throw exception
        try {
            proxy.getConnection();
        } catch(Exception e) {}

        verify(adapter).exceptionCall(Matchers.<RegexMethodInterceptorEvent<DataSource>>any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testReplaceReturnedValue() throws Exception {
        final DataSource actual = mock(DataSource.class);
        doReturn(mock(Connection.class)).when(actual).getConnection();

        final RegexProxyFactory<DataSource> factory = new RegexProxyFactory<DataSource>(DataSource.class);
        factory.addMethodListener("getConnection", new RegexMethodInterceptorAdapter<DataSource>() {
            @Override
            public void afterCall(RegexMethodInterceptorEvent<DataSource> evt) {
                evt.setReturnedValue(null);
            }
        });

        DataSource proxy = factory.getProxy(actual);

        // replace the returned value to null.
        assertNull(proxy.getConnection());
    }

    @Test
    public void testAddMethodListener() throws Exception {
        final DataSource actual = mock(DataSource.class);
        doReturn(mock(Connection.class)).when(actual).getConnection();

        final RegexProxyFactory<DataSource> factory = new RegexProxyFactory<DataSource>(DataSource.class);

        factory.addMethodListener("getConnection", new RegexMethodInterceptorAdapter<DataSource>() {
            @Override
            public void afterCall(RegexMethodInterceptorEvent<DataSource> evt) {
                assertThat(evt.getException(), is(CoreMatchers.<Object>nullValue()));
                assertThat(evt.getReturnedValue(), is(CoreMatchers.<Object>notNullValue()));
                assertThat(evt.getSource(), is(CoreMatchers.<Object>notNullValue()));
                assertThat(evt.getTarget(), is(CoreMatchers.<Object>notNullValue()));
                assertThat(actual, sameInstance(evt.getInvocation().getThis()));
                assertNotNull(evt.getInvocation().getStaticPart());
            }
        });

        DataSource proxy = factory.getProxy(actual);

        // invoked twice
        proxy.getConnection();
        proxy.getConnection();

        assertThat(proxy, not(sameInstance(actual)));
        verify(actual, times(2)).getConnection();
    }

    @Test
    public void testNonInterfaceTest() throws Exception {
        ArrayList<String> list = new ArrayList<String>();
        list.add("hello");

        final RegexProxyFactory<ArrayList> factory = new RegexProxyFactory<ArrayList>(ArrayList.class);
        factory.addMethodListener("contains", new RegexMethodInterceptorAdapter<ArrayList>() {
            @Override
            public void afterCall(RegexMethodInterceptorEvent<ArrayList> evt) {
                evt.setReturnedValue(true);
            }
        });

        ArrayList listProxy = factory.getProxy(list);
        assertEquals(true, listProxy.contains("o"));
    }

    @Test
    public void testDuplicateReturnedValueSet() throws Exception {
        ArrayList<String> list = new ArrayList<String>();
        list.add("hello");

        final RegexProxyFactory<ArrayList> factory = new RegexProxyFactory<ArrayList>(ArrayList.class);
        factory.addMethodListener("c[a-z]*", new RegexMethodInterceptorAdapter<ArrayList>() {
            @Override
            public void afterCall(RegexMethodInterceptorEvent<ArrayList> evt) {
                evt.setReturnedValue(true);
            }
        });
        factory.addMethodListener("contains", new RegexMethodInterceptorAdapter<ArrayList>() {
            @Override
            public void afterCall(RegexMethodInterceptorEvent<ArrayList> evt) {
                evt.setReturnedValue(true);
            }
        });

        // returned value will be reverted back since there were two listeners settings
        // the returned value.
        ArrayList listProxy = factory.getProxy(list);
        assertEquals(false, listProxy.contains("o"));
    }
}
