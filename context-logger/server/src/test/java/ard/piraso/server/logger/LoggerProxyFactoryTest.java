package ard.piraso.server.logger;

import ard.piraso.server.GroupChainId;
import ard.piraso.server.PirasoContext;
import ard.piraso.server.PirasoContextHolder;
import ard.piraso.server.proxy.RegexMethodInterceptorAdapter;
import ard.piraso.server.proxy.RegexMethodInterceptorEvent;
import ard.piraso.server.proxy.RegexProxyFactory;
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

    protected  class LoggerProxyFactory<T> extends AbstractLoggerProxyFactory<T> {
        public LoggerProxyFactory(GroupChainId id, RegexProxyFactory<T> regexProxyFactory) {
            super(id, regexProxyFactory);
        }
    }
}
