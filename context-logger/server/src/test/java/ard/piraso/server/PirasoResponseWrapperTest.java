package ard.piraso.server;

import ard.piraso.api.entry.ResponseEntry;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static junit.framework.Assert.assertEquals;

/**
 * Test for {@link PirasoResponseWrapper} class.
 */
public class PirasoResponseWrapperTest {

    private ResponseEntry entry;

    private PirasoResponseWrapper wrapper;

    @Before
    public void setUp() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        entry = new ResponseEntry();

        wrapper = new PirasoResponseWrapper(response, entry);
    }

    @Test
    public void testAddCookie() throws Exception {
        wrapper.addCookie(new Cookie("name", "value"));
        wrapper.addCookie(new Cookie("name1", "value1"));

        assertEquals(2, CollectionUtils.size(entry.getCookies()));
    }

    @Test
    public void testAddSetDateHeader() throws Exception {
        wrapper.addDateHeader("date", System.currentTimeMillis());
        wrapper.setDateHeader("date2", System.currentTimeMillis());

        assertEquals(2, CollectionUtils.size(entry.getDateHeader()));
    }

    @Test
    public void testAddSetHeader() throws Exception {
        wrapper.addHeader("str", "1");
        wrapper.setHeader("str2", "2");

        assertEquals(2, CollectionUtils.size(entry.getStringHeader()));

    }

    @Test
    public void testAddSetIntHeader() throws Exception {
        wrapper.addIntHeader("int", 1);
        wrapper.setIntHeader("int2", 2);

        assertEquals(2, CollectionUtils.size(entry.getIntHeader()));
    }

    @Test
    public void testSendError() throws Exception {
        wrapper.sendError(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, entry.getStatus());
    }

    @Test
    public void testSendErrorWithReason() throws Exception {
        wrapper.sendError(HttpServletResponse.SC_BAD_GATEWAY, "test");
        assertEquals(HttpServletResponse.SC_BAD_GATEWAY, entry.getStatus());
        assertEquals("test", entry.getStatusReason());
    }

    @Test
    public void testSetStatus() throws Exception {
        wrapper.setStatus(HttpServletResponse.SC_ACCEPTED);
        assertEquals(HttpServletResponse.SC_ACCEPTED, entry.getStatus());

        wrapper.setStatus(HttpServletResponse.SC_FOUND, "found");
        assertEquals(HttpServletResponse.SC_FOUND, entry.getStatus());
        assertEquals("found", entry.getStatusReason());
    }

    @Test
    public void testSetStatusWithReason() throws Exception {
        wrapper.setStatus(HttpServletResponse.SC_FOUND, "found");
        assertEquals(HttpServletResponse.SC_FOUND, entry.getStatus());
        assertEquals("found", entry.getStatusReason());
    }
}
