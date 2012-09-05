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

package ard.piraso.headless.log4j;

import ard.piraso.headless.restriction.Restriction;
import org.apache.log4j.Level;

/**
 * All log4j restrictions
 */
public class Log4jRestrictions {

    public static Restriction logger(String logger) {
        return new Log4jLoggerRestriction(logger);
    }

    public static Restriction infoLevel() {
        return new Log4jLevelRestriction(Level.INFO);
    }

    public static Restriction allLevel() {
        return new Log4jLevelRestriction(Level.ALL);
    }

    public static Restriction debugLevel() {
        return new Log4jLevelRestriction(Level.DEBUG);
    }

    public static Restriction warningLevel() {
        return new Log4jLevelRestriction(Level.WARN);
    }

    public static Restriction fatalLevel() {
        return new Log4jLevelRestriction(Level.FATAL);
    }

    public static Restriction errorLevel() {
        return new Log4jLevelRestriction(Level.ERROR);
    }
}
