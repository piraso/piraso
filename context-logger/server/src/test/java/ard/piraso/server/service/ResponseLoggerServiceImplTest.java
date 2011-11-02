package ard.piraso.server.service;

import ard.piraso.api.GeneralPreferenceEnum;
import ard.piraso.api.Preferences;
import ard.piraso.api.entry.Entry;
import ard.piraso.api.entry.MessageEntry;
import ard.piraso.api.io.EntryReadEvent;
import ard.piraso.api.io.EntryReadListener;
import ard.piraso.api.io.PirasoEntryReader;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test for {@link ResponseLoggerServiceImpl} class
 */
public class ResponseLoggerServiceImplTest {

    private static final String EXPECTED_MONITORED_ADDRESS = "127.0.0.1";

    private ResponseLoggerServiceImpl service;

    private MockHttpServletResponse response;

    private Preferences preferences;

    private User user;

    private MockHttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        request = new MockHttpServletRequest();

        response = spy(new MockHttpServletResponse());
        user = new User(request);

        preferences = new Preferences();
        preferences.addProperty(GeneralPreferenceEnum.STACK_TRACE_ENABLED.getPropertyName(), true);

        request.addParameter("watchedAddr", EXPECTED_MONITORED_ADDRESS);
        request.addParameter("preferences", mapper.writeValueAsString(preferences));

        service = new ResponseLoggerServiceImpl(user, request, response);
    }

    @Test
    public void testMonitoredAddress() throws Exception {
        request.removeParameter("watchedAddr");
        request.setRemoteAddr("remoteAddr");

        service = new ResponseLoggerServiceImpl(user, request, response);
        assertEquals("remoteAddr", service.getWatchedAddr());

        request.addParameter("watchedAddr", EXPECTED_MONITORED_ADDRESS);

        service = new ResponseLoggerServiceImpl(user, request, response);
        assertEquals(EXPECTED_MONITORED_ADDRESS, service.getWatchedAddr());
    }

    @Test
    public void testGetters() throws Exception {
        assertSame(user, service.getUser());
        assertEquals(EXPECTED_MONITORED_ADDRESS, service.getWatchedAddr());
        assertEquals(preferences, service.getPreferences());
        assertEquals(user.getActivityUuid(), service.getId());
        assertTrue(service.isAlive());
    }

    @Test(expected = ForcedStoppedException.class)
    public void testIdleTimeout() throws Exception {
        service.setMaxIdleTimeout(100l);
        service.start();
    }

    @Test(expected = ForcedStoppedException.class)
    public void testMaxTransferSize() throws Exception {
        service.setMaxQueueForceKillSize(2);
        service.log(new MessageEntry(1l, "test"));
        service.log(new MessageEntry(1l, "test2"));

        service.start();
    }

    @Test
    public void testWaitAndStop() throws Exception {
        final AtomicBoolean fail = new AtomicBoolean(false);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable startServiceRunnable = new Runnable() {
            public void run() {
                try {
                    service.start();
                } catch (Exception e) {
                    fail.set(true);
                    e.printStackTrace();
                }
            }
        };

        Runnable logMessagesRunnable = new Runnable() {
            public void run() {
                try {


                    service.stopAndWait(3000l);
                } catch (Exception e) {
                    fail.set(true);
                    e.printStackTrace();
                }
            }
        };

        Future future = executor.submit(startServiceRunnable);
        executor.submit(logMessagesRunnable);

        future.get();
        executor.shutdown();

        if(fail.get()) {
            fail("failure see exception trace.");
        }

        // no harm invoking it again
        service.stopAndWait(1000l);

        assertFalse(service.isAlive());
    }

    @Test
    public void testLogging() throws IOException, TransformerConfigurationException, ParserConfigurationException, ExecutionException, InterruptedException, SAXException {
        final AtomicBoolean fail = new AtomicBoolean(false);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        final List<MessageEntry> expectedEntries = new ArrayList<MessageEntry>() {{
            for(int i = 0; i < 1000; i++) {
                add(new MessageEntry(1l, "test_" + (i + 1)));
            }
        }};

        // stop the service when number of entries is reached.
        stopOnWriteTimes(expectedEntries.size());

        Runnable startServiceRunnable = new Runnable() {
            public void run() {
                try {
                    service.start();
                } catch (Exception e) {
                    fail.set(true);
                    e.printStackTrace();
                }
            }
        };

        Runnable logMessagesRunnable = new Runnable() {
            public void run() {
                try {
                    // this entry should be ignored since this will throw an exception
                    service.log(new ExceptionThrowEntry(1l));

                    // these entries should succeed
                    for(MessageEntry entry : expectedEntries) {
                        service.log(entry);
                    }
                } catch (IOException e) {
                    fail.set(true);
                    e.printStackTrace();
                }
            }
        };

        Future future = executor.submit(startServiceRunnable);
        executor.submit(logMessagesRunnable);

        future.get();
        executor.shutdown();


        if(fail.get()) {
            fail("failure see exception trace.");
        }

        final List<Entry> entriesRead = new ArrayList<Entry>();
        PirasoEntryReader reader = new PirasoEntryReader(new ByteArrayInputStream(response.getContentAsByteArray()));
        reader.addListener(new EntryReadListener() {
            public void readEntry(EntryReadEvent evt) {
                entriesRead.add(evt.getEntry());
            }
        });

        // start reading
        reader.start();

        assertEquals(service.getId(), reader.getId());
        assertEquals(expectedEntries.size(), entriesRead.size());
    }

    /**
     * Helper method to ensure that the service stops when number of logs is reached.
     *
     * @param count the log count before stop
     * @throws UnsupportedEncodingException on error
     */
    private void stopOnWriteTimes(final int count) throws UnsupportedEncodingException {
        PrintWriter writer =  spy(response.getWriter());
        doReturn(writer).when(response).getWriter();
        final AtomicInteger ctr = new AtomicInteger(0);

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                if(ctr.addAndGet(1) > count) {
                    service.stop();
                }

                return invocationOnMock.callRealMethod();
            }
        }).when(writer).println(anyString());
    }

    private class ExceptionThrowEntry extends Entry {

        private ExceptionThrowEntry(Long requestId) {
            setRequestId(requestId);
        }

        public String getPropertyThatThrowException() {
            throw new IllegalStateException("always thrown");
        }
    }
}
