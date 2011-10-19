package ard.piraso.server;

import ard.piraso.api.GeneralPreferenceEnum;

/**
 * General preferences evaluator
 */
public class GeneralPreferenceEvaluator {

    protected ContextPreference preference = new RequestContextPreference();

    private boolean isEnabled(GeneralPreferenceEnum pref) {
        return preference != null && preference.isEnabled(pref.getPropertyName());
    }

    public boolean isStackTraceEnabled() {
        return isEnabled(GeneralPreferenceEnum.STACK_TRACE_ENABLED);
    }
}
