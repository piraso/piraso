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

package org.piraso.api;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Log level preference
 */
public class Level {

    private static final String ALL_LEVEL = "ALL";

    private static final String SCOPE_LEVEL = "general.scoped.enabled";

    private static final Map<String, Level> LEVELS = new HashMap<String, Level>() {{
        put(ALL_LEVEL, new Level(ALL_LEVEL));
        put(SCOPE_LEVEL, new Level(SCOPE_LEVEL));
    }};

    public static Level ALL = Level.get(ALL_LEVEL);

    public static Level SCOPED = Level.get(SCOPE_LEVEL);

    public static Level get(String name) {
        return LEVELS.get(name);
}

    public static boolean isLevel(String name) {
        return LEVELS.containsKey(name);
    }

    public static synchronized Level addLevel(String name) {
        if(LEVELS.containsKey(name)) {
            throw new IllegalArgumentException(String.format("Level with name '%s' already exists.", name));
        }

        Level level = new Level(name);
        LEVELS.put(name, level);

        return level;
    }

    public static void addLevels(PreferenceEnum... enums) {
        for(PreferenceEnum flag : enums) {
            if(flag.isLevel()) {
                Level.addLevel(flag.getPropertyName());
            }
        }
    }

    private final String name;

    private Level(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
