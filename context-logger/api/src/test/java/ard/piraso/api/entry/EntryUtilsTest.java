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

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Test for {@link EntryUtils} class.
 */
public class EntryUtilsTest {

    @Test(expected = IllegalAccessException.class)
    public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        EntryUtils.class.newInstance();
    }

    @Test
    public void testPrivateConstructorWithAccessibility() throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Constructor constructor = EntryUtils.class.getDeclaredConstructors()[0];

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        // force invoking private constructor
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testToEntryNullEntries() throws Exception {
        Object[] obj = null;
        StackTraceElement[] st = null;

        assertNull(EntryUtils.toEntry(obj));
        assertNull(EntryUtils.toEntry(st));
    }

    @Test
    public void testToEntryEmpty() throws Exception {
        Object[] obj = new Object[0];
        StackTraceElement[] st = new StackTraceElement[0];

        assertTrue(EntryUtils.toEntry(obj).length == 0);
        assertTrue(EntryUtils.toEntry(st).length == 0);
    }

    @Test
    public void testToEntryMoreThanZero() throws Exception {
        Object[] obj = new Object[] {"test"};
        StackTraceElement[] st = Thread.currentThread().getStackTrace();

        assertTrue(EntryUtils.toEntry(obj).length == obj.length);
        assertTrue(EntryUtils.toEntry(st).length == st.length);
    }
}
