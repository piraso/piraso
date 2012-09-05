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

import ard.piraso.api.GeneralPreferenceEnum;
import ard.piraso.api.Preferences;

/**
 * general preference builder
 */
public class GeneralPreferenceWrapper {

    private Preferences pref;

    public GeneralPreferenceWrapper() {
        this(new Preferences());
    }

    public GeneralPreferenceWrapper(Preferences pref) {
        this.pref = pref;
    }

    public void disableRequestScope() {
        pref.addProperty(GeneralPreferenceEnum.NO_REQUEST_CONTEXT.getPropertyName(), true);
    }

    public void enableStackTrace() {
        pref.addProperty(GeneralPreferenceEnum.STACK_TRACE_ENABLED.getPropertyName(), true);
    }

    public void hideExternalResources() {
        pref.addProperty(GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName(), true);
    }

    public Preferences getPreferences() {
        return pref;
    }
}
