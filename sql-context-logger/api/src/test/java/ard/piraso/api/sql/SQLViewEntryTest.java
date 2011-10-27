package ard.piraso.api.sql;

import ard.piraso.api.entry.ElapseTimeEntry;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link SQLViewEntry} class.
 */
public class SQLViewEntryTest extends AbstractJacksonTest {

    @Test
    public void testJackson() throws IOException, InterruptedException, NoSuchMethodException {
        Map<Integer, SQLParameterEntry> parameters = new LinkedHashMap<Integer, SQLParameterEntry>();

        Method method = PreparedStatement.class.getMethod("setString", new Class[] { Integer.TYPE, String.class });
        parameters.put(1, new SQLParameterEntry(1, method, new Object[]{1, "test"}));

        SQLViewEntry expected = new SQLViewEntry("select 1 from dual where x=?", parameters,
                new ElapseTimeEntry(System.currentTimeMillis(), System.currentTimeMillis() + 4000l));

        String jsonValue = mapper.writeValueAsString(expected);
        SQLViewEntry actual = mapper.readValue(jsonValue, SQLViewEntry.class);

        assertEquals(expected, actual);
    }
}
