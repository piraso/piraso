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

package ard.piraso.api.log4j;

import ard.piraso.api.Level;
import ard.piraso.api.PreferenceEnum;

/**
 * Log4j preference enumeration
 */
public enum Log4jPreferenceEnum implements PreferenceEnum {
    LOG4J_ENABLED("log4j.enabled");

    // register enum as level
    static {
        Level.addLevels(values());
    }

    private String propertyName;

    private boolean level;

    private Log4jPreferenceEnum(String propertyName) {
        this(propertyName, true);
    }

    private Log4jPreferenceEnum(String propertyName, boolean level) {
        this.propertyName = propertyName;
        this.level = level;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public boolean isLevel() {
        return level;
    }
}
