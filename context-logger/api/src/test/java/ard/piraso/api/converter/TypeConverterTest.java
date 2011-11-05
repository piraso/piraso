/*
 * Copyright (c) 2011. Piraso Alvin R. de Leon. All Rights Reserved.
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
