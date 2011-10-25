package ard.piraso.server.logger;

import ard.piraso.api.entry.ElapseTimeEntry;
import ard.piraso.api.entry.MessageEntry;
import ard.piraso.server.proxy.ProxyInterceptorAware;
import ard.piraso.server.proxy.RegexProxyFactory;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test for {@link MessageLoggerListener} class.
 */
public class MessageLoggerListenerTest extends AbstractLoggerListenerTest {

    @Test
    public void testProxy() throws SQLException {
        RegexProxyFactory<Connection> rf = new RegexProxyFactory<Connection>(Connection.class);
        LoggerProxyFactory<Connection> factory = new LoggerProxyFactory<Connection>(id, rf);

        MessageLoggerListener<Connection> listener = new MessageLoggerListener<Connection>(null, new TraceableID("test"), "test");
        rf.addMethodListener("close", listener);

        Connection connection = mock(Connection.class);
        Connection proxy = factory.getProxy(connection);

        proxy.close();

        assertTrue(MessageEntry.class.isInstance(caughtEntry));
        verify(context, times(1)).log(anyString(), any(TraceableID.class), any(MessageEntry.class));
    }

    @Test
    public void testProxyWithElapseTime() throws SQLException {
        RegexProxyFactory<Connection> factory = new RegexProxyFactory<Connection>(Connection.class);

        Connection connection = mock(Connection.class);
        ProxyInterceptorAware<Connection> proxy = factory.getProxyInterceptor(connection);

        ElapseTimeEntry elapseTime = new ElapseTimeEntry();
        elapseTime.start();
        MessageLoggerListener<Connection> listener = new MessageLoggerListener<Connection>(null, new TraceableID("test"), "test", elapseTime);

        proxy.getInterceptor().addMethodListener("close", listener);
        proxy.getProxy().close();

        assertTrue(MessageEntry.class.isInstance(caughtEntry));
        verify(context, times(1)).log(anyString(), any(TraceableID.class), any(MessageEntry.class));
    }
}
