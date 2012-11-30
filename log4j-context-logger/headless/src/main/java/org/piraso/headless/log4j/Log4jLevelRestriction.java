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

package org.piraso.headless.log4j;

import org.piraso.api.entry.Entry;
import org.piraso.api.log4j.Log4jEntry;
import org.piraso.headless.restriction.Restriction;
import org.apache.log4j.Level;

/**
 * Checks for log4j level restriction.
 */
public class Log4jLevelRestriction implements Restriction {

    private Level level;

    public Log4jLevelRestriction(Level level) {
        this.level = level;
    }

    public boolean matches(Entry entry) {
        if(!Log4jEntry.class.isInstance(entry)) {
            return false;
        }

        Log4jEntry log4jEntry = (Log4jEntry) entry;

        return log4jEntry.getLogLevel().equals(level.toString());
    }
}
