package ard.piraso.server.logger;

import ard.piraso.api.GeneralPreferenceEnum;
import ard.piraso.api.Level;
import ard.piraso.api.entry.MessageEntry;
import ard.piraso.api.entry.MethodCallEntry;
import ard.piraso.server.AbstractLoggerListenerTest;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.proxy.ProxyInterceptorAware;
import ard.piraso.server.proxy.RegexProxyFactory;
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
