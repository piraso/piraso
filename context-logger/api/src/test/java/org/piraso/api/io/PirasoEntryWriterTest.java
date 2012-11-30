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

package org.piraso.api.io;

import org.piraso.api.entry.MessageEntry;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertTrue;

/**
 * Test for {@link PirasoEntryWriter} classes.
 */
public class PirasoEntryWriterTest {

    @Test
    public void testWrite() throws Exception {
        StringWriter buf = new StringWriter();

        PirasoEntryWriter writer = new PirasoEntryWriter("1", "2", new PrintWriter(buf));
        writer.write(new MessageEntry(1l, "message"));
        writer.close();

        String actual = buf.toString();

        System.out.println(actual);
        assertTrue(actual.contains("id=\"1\""));
        assertTrue(actual.contains("watched-address=\"2\""));
        assertTrue(actual.contains("message"));
        assertTrue(actual.contains(MessageEntry.class.getName()));
    }
}
