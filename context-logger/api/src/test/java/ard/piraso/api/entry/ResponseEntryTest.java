package ard.piraso.api.entry;

import ard.piraso.api.AbstractJacksonTest;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link ResponseEntry} class.
 */
public class ResponseEntryTest extends AbstractJacksonTest {
    @Test
    public void testJackson() throws IOException {
        ResponseEntry expected = new ResponseEntry();

        expected.setStatus(1);
        expected.setStatusReason("");
        expected.setElapseTime(new ElapseTimeEntry());

        expected.addCookie(createCookie());
        expected.addCookie(createCookie());

        expected.addHeader("1", 1);
        expected.addHeader("2", 2);
        expected.addHeader("3", "");
        expected.addHeader("4", "");
        expected.addHeader("5", 5l);
        expected.addHeader("6", 6l);

        String jsonValue = mapper.writeValueAsString(expected);
        ResponseEntry actual = mapper.readValue(jsonValue, ResponseEntry.class);

        assertThat("same entry", actual, is(expected));

    }

    private CookieEntry createCookie() {
        CookieEntry entry = new CookieEntry();
        entry.setMaxAge(1);
        entry.setPath("/");
        entry.setSecure(true);
        entry.setVersion(1);
        entry.setComment("comment");
        entry.setDomain("domain");

        return entry;
    }
}
