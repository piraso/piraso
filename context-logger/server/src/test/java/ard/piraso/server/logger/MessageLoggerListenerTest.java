package ard.piraso.server.logger;

import ard.piraso.api.Level;
import ard.piraso.api.entry.ElapseTimeEntry;
import ard.piraso.api.entry.MessageEntry;
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
