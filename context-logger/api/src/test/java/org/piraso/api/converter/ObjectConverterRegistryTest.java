/*
 * Copyright (c) 2012. Piraso Alvin R. de Leon. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The Piraso licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.piraso.api.converter;

import org.junit.Test;

import java.sql.Timestamp;
import java.util.StringTokenizer;

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

        assertFalse(ObjectConverterRegistry.isSupported(new Object()));
        assertFalse(ObjectConverterRegistry.isSupported(new SampleBean("test")));

        ObjectConverterRegistry.register(SampleBean.class, new TypeConverter<SampleBean>(SampleBean.class));

        assertTrue(ObjectConverterRegistry.isSupported(new SampleBean("test")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsSupportedNull() throws Exception {
        ObjectConverterRegistry.isSupported(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testToStringNull() throws Exception {
        ObjectConverterRegistry.convertToString(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testToStringClassNotSupported() throws Exception {
        ObjectConverterRegistry.convertToString(new StringTokenizer(""));
    }

    @Test(expected = IllegalStateException.class)
    public void testToObjectNotSupportedClass() throws Exception {
        ObjectConverterRegistry.convertToObject(StringBuilder.class.getName(), "");
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

        final Timestamp expectedTimestamp = new Timestamp(System.currentTimeMillis());
        String timestampConvertedValue = ObjectConverterRegistry.convertToString(expectedTimestamp);
        Timestamp actualTimestamp = (Timestamp) ObjectConverterRegistry.convertToObject(Timestamp.class.getName(), timestampConvertedValue);
        assertThat(actualTimestamp, is(expectedTimestamp));

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
