package ard.piraso.server;

import ard.piraso.api.entry.Entry;
import ard.piraso.server.dispatcher.ContextLogDispatcher;
import ard.piraso.server.logger.AbstractLoggerProxyFactory;
import ard.piraso.server.logger.TraceableID;
import ard.piraso.server.proxy.RegexProxyFactory;
import org.junit.Before;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * Base class for logger listener
 */
public abstract class AbstractLoggerListenerTest {

    protected PirasoContext context;

    protected Entry caughtEntry;

    protected TraceableID id;

    @Before
    public void setUp() throws Exception {
        id = new TraceableID("test");
        context = mock(PirasoContext.class);

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                caughtEntry = (Entry) invocationOnMock.getArguments()[2];
                return invocationOnMock.callRealMethod();
            }
        }).when(context).log(anyString(), any(TraceableID.class), any(Entry.class));

        PirasoContextHolder.setContext(context);
        ContextLogDispatcher.clearListeners();
    }

    protected  class LoggerProxyFactory<T> extends AbstractLoggerProxyFactory<T> {
        public LoggerProxyFactory(TraceableID id, RegexProxyFactory regexProxyFactory) {
            super(id, regexProxyFactory);
        }
    }
}
