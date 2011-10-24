package ard.piraso.server;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test for {@link RequestContextPreference} class.
 */
public class RequestContextPreferenceTest {

    private PirasoContext context;

    private RequestContextPreference requestContext;

    @Before
    public void setUp() throws Exception {
        context = mock(PirasoContext.class);
        PirasoContextHolder.setContext(context);

        requestContext = new RequestContextPreference();
    }

    @Test
    public void testNullDelegate() {
        PirasoContextHolder.removeContext();
        requestContext = new RequestContextPreference();

        assertFalse(requestContext.isMonitored());
        assertFalse(requestContext.isEnabled("some property"));
        assertNull(requestContext.getIntValue("some property"));

        requestContext.requestOnScope(); // nothing will happen here

        verify(context, times(0)).isMonitored();
        verify(context, times(0)).isEnabled("some property");
        verify(context, times(0)).getIntValue("some property");
        verify(context, times(0)).requestOnScope();
    }

    @Test
    public void testIsMonitored() throws Exception {
        doReturn(true).when(context).isMonitored();

        assertTrue(requestContext.isMonitored());
        verify(context, times(1)).isMonitored();
    }

    @Test
    public void testIsEnabled() throws Exception {
        doReturn(true).when(context).isEnabled("property");

        assertTrue(requestContext.isEnabled("property"));
        assertFalse(requestContext.isEnabled("other property"));

        verify(context, times(2)).isEnabled(anyString());
    }

    @Test
    public void testGetIntValue() throws Exception {
        doReturn(1).when(context).getIntValue("property");

        assertEquals(Integer.valueOf(1), requestContext.getIntValue("property"));
        assertEquals(Integer.valueOf(0), requestContext.getIntValue("other property"));

        verify(context, times(2)).getIntValue(anyString());
    }
}
