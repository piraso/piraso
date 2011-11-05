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

package ard.piraso.server;

import ard.piraso.api.GeneralPreferenceEnum;

/**
 * General preferences evaluator
 */
public class GeneralPreferenceEvaluator {

    protected ContextPreference preference = new PirasoRequestContext();

    private boolean isEnabled(GeneralPreferenceEnum pref) {
        return preference.isEnabled(pref.getPropertyName());
    }

    public boolean isStackTraceEnabled() {
        return isEnabled(GeneralPreferenceEnum.STACK_TRACE_ENABLED);
    }

    public boolean isLoggingScopedEnabled() {
        return isEnabled(GeneralPreferenceEnum.SCOPE_ENABLED);
    }

    public void requestOnScope() {
        preference.requestOnScope();
    }
}
