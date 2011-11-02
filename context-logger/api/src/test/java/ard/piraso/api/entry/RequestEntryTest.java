package ard.piraso.api.entry;

import ard.piraso.api.AbstractJacksonTest;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedHashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link RequestEntry} class.
 */
public class RequestEntryTest extends AbstractJacksonTest {
    @Test
    public void testJackson() throws IOException {
        RequestEntry expected = new RequestEntry("/");
        expected.setMethod("GET");
        expected.setParameters(new LinkedHashMap<String, String[]>());
        expected.setQueryString("test=1");
        expected.setRemoteAddr("127.0.0.1");

        expected.addCookie(createCookie());
        expected.addCookie(createCookie());
        expected.addHeader("test", "test");
        expected.addHeader("test2", "test");

        String jsonValue = mapper.writeValueAsString(expected);
        RequestEntry actual = mapper.readValue(jsonValue, RequestEntry.class);

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
