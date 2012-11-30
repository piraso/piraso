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

package org.piraso.api;

import org.junit.Test;

import java.util.HashSet;

import static junit.framework.Assert.*;

/**
 * Test for {@link Level} class.
 */
public class LevelTest {

    @Test
    public void testIsLevelAndGet() throws Exception {
        assertTrue(Level.isLevel(Level.ALL.getName()));
        assertTrue(Level.isLevel(Level.SCOPED.getName()));
        assertFalse(Level.isLevel("notALevel"));

        assertEquals(Level.ALL, Level.get(Level.ALL.getName()));
        assertNull(Level.get("NotALevel"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLevelAlreadyExists() throws Exception {
        Level.addLevel(Level.ALL.getName());
    }

    @Test
    public void testAddLevelSuccess() throws Exception {
        Level.addLevel("test");

        assertNotNull(Level.get("test"));
    }

    @Test
    public void testHashCode() throws Exception {
        HashSet<Level> levels = new HashSet<Level>();

        levels.add(Level.ALL);
        levels.add(Level.ALL);
        levels.add(Level.SCOPED);

        assertEquals(2, levels.size());
    }
}
