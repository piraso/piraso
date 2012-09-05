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

package ard.piraso.headless;

import ard.piraso.api.Preferences;
import ard.piraso.api.entry.Entry;
import ard.piraso.api.log4j.Log4jPreferenceWrapper;
import ard.piraso.headless.log4j.Log4jRestrictions;
import ard.piraso.headless.restriction.Restrictions;
import org.junit.Test;

import java.util.List;

/**
 * Test for {@link PirasoHeadless}
 */
public class PirasoHeadlessTest {

    @Test
    public void testSimpleTest() throws Exception {
        Preferences preferences = new Preferences();

        new GeneralPreferenceWrapper(preferences)
                .disableRequestScope()
                .hideExternalResources();

        new Log4jPreferenceWrapper(preferences)
                .all("browserEvent");

        PirasoHeadless piraso = PirasoHeadless.create(preferences, "http://dev-ws01.adchemy.colo:8180/piraso/logging");

        try {
            piraso.start();
            Thread.sleep(10000);

            List<Entry> entries = piraso.createCriteria()
                    .add(
                            Restrictions.or()
                                .add(Log4jRestrictions.allLevel())
                                .add(Log4jRestrictions.infoLevel())
                                .add(Log4jRestrictions.warningLevel())
                    )
                    .add(Log4jRestrictions.logger("browserEvent"))
                    .list();

            System.out.println(entries.size());

            piraso.stop();
        } finally {
            piraso.shutdown();
        }
    }
}
