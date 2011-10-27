package ard.piraso.api.sql;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link SQLDataViewEntry} class.
 */
public class SQLDataViewEntryTest extends AbstractJacksonTest {

    @Test
    public void testJackson() throws IOException, InterruptedException, NoSuchMethodException {
        List<List<SQLParameterEntry>> records = new ArrayList<List<SQLParameterEntry>>();

        Method method = ResultSet.class.getMethod("getString", new Class[] { String.class });
        records.add(Arrays.asList(new SQLParameterEntry("column1", method, new Object[] {"column1"}, "test")));

        SQLDataViewEntry expected = new SQLDataViewEntry(1l, records);

        String jsonValue = mapper.writeValueAsString(expected);
        SQLDataViewEntry actual = mapper.readValue(jsonValue, SQLDataViewEntry.class);

        assertEquals(expected, actual);
    }
}
