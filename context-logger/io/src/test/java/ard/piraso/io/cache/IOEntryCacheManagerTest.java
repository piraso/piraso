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

package ard.piraso.io.cache;

import ard.piraso.api.JacksonUtils;
import ard.piraso.api.entry.MessageEntry;
import ard.piraso.io.util.IOEntrySerializable;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertNotNull;

/**
 * Test for {@link IOEntryCacheManager} class.
 */
public class IOEntryCacheManagerTest {
    @Test
    public void testAdd() throws Exception {
        IOEntryCacheManager manager = IOEntryCacheManager.INSTANCE;

        ObjectMapper mapper = JacksonUtils.MAPPER;

        MessageEntry entry = new MessageEntry(1l, "test");

        IOEntrySerializable serializable = new IOEntrySerializable();
        serializable.setClassType(MessageEntry.class.getName());
        serializable.setDate(new Date());
        serializable.setEntryValue(mapper.writeValueAsString(entry));
        serializable.setId(entry.getRequestId());
        serializable.setRowNum(1l);

        manager.add("a", 1, serializable);

        IOEntrySerializable actual = manager.get("a", 1, 1);

        assertNotNull(actual);
    }

    @Test
    public void testGet() throws Exception {

    }
}
