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

package org.piraso.api.sql;

import org.piraso.api.entry.ElapseTimeEntry;
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
