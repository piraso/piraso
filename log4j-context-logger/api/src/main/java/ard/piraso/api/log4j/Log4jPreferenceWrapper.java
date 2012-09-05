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

import ard.piraso.api.Preferences;
import org.apache.log4j.Level;

import java.util.List;

/**
 * Preference wrapper for log4j
 */
public class Log4jPreferenceWrapper {

    private Preferences pref;

    public Log4jPreferenceWrapper(Preferences pref) {
        this.pref = pref;
    }

    public void setAllLoggers(List<String> loggers) {
        for(String logger : loggers) {
            all(logger);
        }
    }

    public void setInfoLoggers(List<String> loggers) {
        for(String logger : loggers) {
            info(logger);
        }
    }

    public void setWarningLoggers(List<String> loggers) {
        for(String logger : loggers) {
            warn(logger);
        }
    }

    public void setErrorLoggers(List<String> loggers) {
        for(String logger : loggers) {
            error(logger);
        }
    }

    public void setDebugLoggers(List<String> loggers) {
        for(String logger : loggers) {
            debug(logger);
        }
    }

    public void setFatalLoggers(List<String> loggers) {
        for(String logger : loggers) {
            fatal(logger);
        }
    }

    public Log4jPreferenceWrapper info(String logger) {
        return add(logger, Level.INFO);
    }

    public Log4jPreferenceWrapper debug(String logger) {
        return add(logger, Level.DEBUG);
    }

    public Log4jPreferenceWrapper warn(String logger) {
        return add(logger, Level.WARN);
    }

    public Log4jPreferenceWrapper error(String logger) {
        return add(logger, Level.ERROR);
    }

    public Log4jPreferenceWrapper fatal(String logger) {
        return add(logger, Level.FATAL);
    }

    public Log4jPreferenceWrapper all(String logger) {
        return add(logger, Level.ALL);
    }

    public Log4jPreferenceWrapper add(String logger, Level level) {
        pref.addProperty("log4j." + logger + "." + level.toString(), true);

        return this;
    }
}
