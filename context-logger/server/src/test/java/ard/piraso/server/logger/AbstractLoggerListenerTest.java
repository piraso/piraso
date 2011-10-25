package ard.piraso.server.logger;

import ard.piraso.api.entry.Entry;
import ard.piraso.server.PirasoContext;
import ard.piraso.server.PirasoContextHolder;
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

    @Before
    public void setUp() throws Exception {
        context = mock(PirasoContext.class);

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                caughtEntry = (Entry) invocationOnMock.getArguments()[2];
                return invocationOnMock.callRealMethod();
            }
        }).when(context).log(anyString(), any(TraceableID.class), any(Entry.class));

        PirasoContextHolder.setContext(context);
    }
}
