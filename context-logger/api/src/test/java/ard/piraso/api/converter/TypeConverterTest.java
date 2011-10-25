package ard.piraso.api.converter;

import org.junit.Test;

import static junit.framework.Assert.assertNull;

/**
 * Test for {@link TypeConverter} class.
 */
public class TypeConverterTest {
    @Test
    public void testConvertToStringWithNullArg() throws Exception {
        String value = new TypeConverter<Integer>(Integer.class).convertToString(null);

        assertNull(value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertToStringWithWrongInstanceArg() throws Exception {
        new TypeConverter<Integer>(Integer.class).convertToString("hello");
    }

    @Test
    public void testConvertToObjectWithNullArg() throws Exception {
        Integer value = (Integer) new TypeConverter<Integer>(Integer.class).convertToObject(null);

        assertNull(value);
    }
}
