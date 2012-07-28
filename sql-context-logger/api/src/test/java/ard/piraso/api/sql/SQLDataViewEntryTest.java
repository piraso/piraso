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

package ard.piraso.api.sql;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link SQLDataViewEntry} class.
 */
public class SQLDataViewEntryTest extends AbstractJacksonTest {

    @Test
    public void testJackson() throws IOException, InterruptedException, NoSuchMethodException {
        List<List<SQLParameterEntry>> records = new ArrayList<List<SQLParameterEntry>>();

        Method method = ResultSet.class.getMethod("getString", new Class[] { String.class });
        records.add(Arrays.asList(new SQLParameterEntry("column1", method, new Object[] {"column1"})));

        SQLDataViewEntry expected = new SQLDataViewEntry(1l, records);

        String jsonValue = mapper.writeValueAsString(expected);
        SQLDataViewEntry actual = mapper.readValue(jsonValue, SQLDataViewEntry.class);

        assertEquals(expected, actual);
    }
}
