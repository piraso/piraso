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

package org.piraso.headless.restriction;

import java.util.regex.Pattern;

/**
 * All restriction instance
 */
public class Restrictions {

    public static DisjunctionRestriction disjunction() {
        return new DisjunctionRestriction();
    }

    public static Restriction clazz(Class clazz) {
        return new ClassRestriction(clazz);
    }

    public static Restriction group(String group) {
        return new GroupRestriction(group);
    }

    public static Restriction messageRegex(String regex) {
        return messageRegex(regex, false);
    }

    public static Restriction messageRegex(String regex, boolean caseInsensitive) {
        if(caseInsensitive) {
            return new MessageRegexRestriction(regex, Pattern.CASE_INSENSITIVE);
        }

        return new MessageRegexRestriction(regex);
    }
}
