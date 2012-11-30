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

package org.piraso.api.entry;

import org.piraso.api.AbstractJacksonTest;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link GroupEntry} class.
 */
public class GroupEntryTest  extends AbstractJacksonTest {
    @Test
    public void testJackson() throws IOException {
        LinkedList<String> ids = new LinkedList<String>() {{
            add("1");
            add("2");
        }};

        GroupEntry expected = new GroupEntry(ids);

        String jsonValue = mapper.writeValueAsString(expected);
        GroupEntry actual = mapper.readValue(jsonValue, GroupEntry.class);

        assertThat("same entry", actual, is(expected));

    }
}
