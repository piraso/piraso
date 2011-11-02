package ard.piraso.api.entry;

import ard.piraso.api.AbstractJacksonTest;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link GroupEntry} class.
 */
public class GroupEntryTest  extends AbstractJacksonTest {
    @Test
    public void testJackson() throws IOException {
        LinkedList<String> ids = new LinkedList<String>() {{
            add("1");
            add("2");
        }};

        GroupEntry expected = new GroupEntry(ids);

        String jsonValue = mapper.writeValueAsString(expected);
        GroupEntry actual = mapper.readValue(jsonValue, GroupEntry.class);

        assertThat("same entry", actual, is(expected));

    }
}
