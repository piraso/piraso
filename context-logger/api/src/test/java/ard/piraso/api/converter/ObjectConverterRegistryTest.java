package ard.piraso.api.converter;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;

/**
 * Test for {@link ObjectConverterRegistry} class.
 */
public class ObjectConverterRegistryTest {

    @Test
    public void testIsSupported() throws Exception {
        assertTrue(ObjectConverterRegistry.isSupported("test"));
        assertTrue(ObjectConverterRegistry.isSupported('a'));
        assertTrue(ObjectConverterRegistry.isSupported(1));
        assertTrue(ObjectConverterRegistry.isSupported(Byte.valueOf("1")));
        assertTrue(ObjectConverterRegistry.isSupported(Short.valueOf("1")));
        assertTrue(ObjectConverterRegistry.isSupported(1.0f));
        assertTrue(ObjectConverterRegistry.isSupported(1.0));
        assertTrue(ObjectConverterRegistry.isSupported(Integer.class));

        assertFalse(ObjectConverterRegistry.isSupported(new StringBuffer()));
        assertFalse(ObjectConverterRegistry.isSupported(new Object()));
        assertFalse(ObjectConverterRegistry.isSupported(new SampleBean("test")));

        ObjectConverterRegistry.register(SampleBean.class, new TypeConverter<SampleBean>(SampleBean.class));

        assertTrue(ObjectConverterRegistry.isSupported(new SampleBean("test")));
    }

    @Test
    public void testTypeConversion() throws Exception {
        final String expectedString = "test";

        String stringConvertedValue = ObjectConverterRegistry.convertToString(expectedString);
        String actualString = (String) ObjectConverterRegistry.convertToObject(String.class.getName(), stringConvertedValue);
        assertThat(actualString, is(expectedString));

        final Integer expectedInteger = 12;
        String integerConvertedValue = ObjectConverterRegistry.convertToString(expectedInteger);
        Integer actualInteger = (Integer) ObjectConverterRegistry.convertToObject(Integer.class.getName(), integerConvertedValue);
        assertThat(actualInteger, is(expectedInteger));

        final Double expectedDouble = 12.0;
        String doubleConvertedValue = ObjectConverterRegistry.convertToString(expectedDouble);
        Double actualDouble = (Double) ObjectConverterRegistry.convertToObject(Double.class.getName(), doubleConvertedValue);
        assertThat(actualDouble, is(expectedDouble));

        final Boolean expectedBoolean = false;
        String booleanConvertedValue = ObjectConverterRegistry.convertToString(expectedBoolean);
        Boolean actualBoolean = (Boolean) ObjectConverterRegistry.convertToObject(Boolean.class.getName(), booleanConvertedValue);
        assertThat(actualBoolean, is(expectedBoolean));

        final Class expectedClass = Integer.class;
        String classConvertedValue = ObjectConverterRegistry.convertToString(expectedClass);
        Class actualClass = (Class) ObjectConverterRegistry.convertToObject(Class.class.getName(), classConvertedValue);
        assertThat(actualClass, sameInstance(expectedClass));
    }

    @Test
    public void testBeanConversion() throws Exception {
        ObjectConverterRegistry.register(SampleBean.class, new TypeConverter<SampleBean>(SampleBean.class));

        SampleBean expected = new SampleBean("sample");

        String convertedValue = ObjectConverterRegistry.convertToString(expected);
        SampleBean actual = (SampleBean) ObjectConverterRegistry.convertToObject(SampleBean.class.getName(), convertedValue);
        assertThat(actual, is(expected));
    }

    private static class SampleBean {
        private String property;

        private SampleBean() {}

        private SampleBean(String property) {
            this.property = property;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SampleBean that = (SampleBean) o;

            if (property != null ? !property.equals(that.property) : that.property != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return property != null ? property.hashCode() : 0;
        }
    }
}
