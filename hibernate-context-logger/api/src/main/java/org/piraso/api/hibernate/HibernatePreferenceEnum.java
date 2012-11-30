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

package org.piraso.api.hibernate;

import org.piraso.api.Level;
import org.piraso.api.PreferenceEnum;

/**
 * Hibernate preferences enumeration
 */
public enum HibernatePreferenceEnum implements PreferenceEnum {
    SESSION_ENABLED("hibernate.session.enabled"),

    SESSION_METHOD_CALL_ENABLED("hibernate.session.method.call.enabled");

    // register enum as level
    static {
        Level.addLevels(values());
    }

    private String propertyName;

    private boolean level;

    private HibernatePreferenceEnum(String propertyName) {
        this(propertyName, true);
    }

    private HibernatePreferenceEnum(String propertyName, boolean level) {
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
