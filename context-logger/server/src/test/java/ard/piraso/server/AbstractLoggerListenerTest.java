package ard.piraso.server;

import ard.piraso.api.Level;
import ard.piraso.api.entry.Entry;
import ard.piraso.server.dispatcher.ContextLogDispatcher;
import org.junit.Before;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * Base class for logger listener
 */
public abstract class AbstractLoggerListenerTest {

    protected PirasoContext context;

    protected Entry caughtEntry;

    protected GroupChainId id;

    @Before
    public void setUp() throws Exception {
        id = new GroupChainId("test");
        context = mock(PirasoContext.class);

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                caughtEntry = (Entry) invocationOnMock.getArguments()[2];
                return invocationOnMock.callRealMethod();
            }
        }).when(context).log(Matchers.<Level>any(), any(GroupChainId.class), any(Entry.class));

        PirasoContextHolder.setContext(context);
        ContextLogDispatcher.clearListeners();
    }
}
