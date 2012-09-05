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

package ard.piraso.io;

import org.junit.Test;

/**
 * Test for {@link IOEntryReader} class.
 */
public class IOEntryReaderTest {
    @Test
    public void testStart() throws Exception {
//        Preferences pref = new Preferences();
//        pref.addProperty(GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName(), true);
//        pref.addProperty("sql.connection.enabled", true);
//
//        HttpEntrySource source = new HttpEntrySource(pref, "http://127.0.0.1:8080/piraso/logging");
//        IOEntryReader reader = new IOEntryReader(source);
//
//        reader.addListener(new IOEntryListener() {
//            @Override
//            public void started(IOEntryEvent evt) {
//                System.out.println(String.format("started(id: %s, ip: %s)", evt.getId(), evt.getWatchedAddr()));
//            }
//
//            @Override
//            public void receivedEntry(IOEntryEvent evt) {
//                try {
//                    System.out.println("Entry: [" + evt.getEntry().getEntry().getClass().getSimpleName() + "]" + new ObjectMapper().writeValueAsString(evt.getEntry()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        reader.start();
    }

    @Test
    public void testStop() throws Exception {

    }
}
