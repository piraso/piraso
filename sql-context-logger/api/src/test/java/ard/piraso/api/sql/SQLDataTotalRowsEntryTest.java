package ard.piraso.api.sql;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link SQLDataTotalRowsEntry} class.
 */
public class SQLDataTotalRowsEntryTest extends AbstractJacksonTest {

    @Test
    public void testJackson() throws IOException, InterruptedException {
        SQLDataTotalRowsEntry expected = new SQLDataTotalRowsEntry(12);

        String jsonValue = mapper.writeValueAsString(expected);
        SQLDataTotalRowsEntry actual = mapper.readValue(jsonValue, SQLDataTotalRowsEntry.class);

        assertEquals(expected, actual);
    }
}
