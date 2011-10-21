package ard.piraso.server.proxy;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
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
