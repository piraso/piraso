package ard.piraso.server.proxy;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.mockito.Matchers;

import javax.sql.DataSource;
import java.sql.Connection;

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
}
