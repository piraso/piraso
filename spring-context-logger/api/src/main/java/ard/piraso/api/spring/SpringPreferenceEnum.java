/*
 * Copyright (c) 2011. Piraso Alvin R. de Leon. All Rights Reserved.
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

package ard.piraso.api.spring;

import ard.piraso.api.Level;
import ard.piraso.api.PreferenceEnum;

/**
 * Spring preference enumeration
 */
public enum SpringPreferenceEnum implements PreferenceEnum {
    /**
     * Determines whether spring remoting is enabled
     */
    REMOTING_ENABLED("spring.remoting.enabled"),

    /**
     * Determines whether spring remoting elapse time is computed
     */
    REMOTING_ELAPSE_TIME_ENABLED("spring.remoting.elapse.time.enabled"),

    /**
     * Determines whether spring remoting method call is enabled
     */
    REMOTING_METHOD_CALL_ENABLED("spring.remoting.method.call.enabled");

    static {
        Level.addLevels(SpringPreferenceEnum.values());
    }

    private String propertyName;

    private boolean level;

    private SpringPreferenceEnum(String propertyName) {
        this(propertyName, true);
    }

    private SpringPreferenceEnum(String propertyName, boolean level) {
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
