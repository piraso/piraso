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

package ard.piraso.api.entry;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.StringTokenizer;

import static junit.framework.Assert.*;

/**
 * Test for {@link ObjectEntryUtils} class.
 */
public class ObjectEntryUtilsTest {

    @Test(expected = IllegalAccessException.class)
    public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        ObjectEntryUtils.class.newInstance();
    }

    @Test
    public void testPrivateConstructorWithAccessibility() throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Constructor constructor = ObjectEntryUtils.class.getDeclaredConstructors()[0];

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        // force invoking private constructor
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testToObjectNull() {
        assertNull(ObjectEntryUtils.toObject(null));
        assertNull(ObjectEntryUtils.toObject(new ObjectEntry(null)));
    }

    @Test(expected = IllegalStateException.class)
    public void testToObjectNotSupported() {
        ObjectEntryUtils.toObject(new ObjectEntry(new StringTokenizer("")));
    }

    @Test
    public void testToObjectValid() throws Exception {
        String test = "test";
        ObjectEntry entry = new ObjectEntry("test");

        assertEquals(ObjectEntryUtils.toObject(entry), test);
        assertEquals(entry.toObject(), test);
    }
}
