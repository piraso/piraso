package ard.piraso.api;

/**
 * General preference enumeration
 */
public enum GeneralPreferenceEnum {
    STACK_TRACE_ENABLED("general.stack.trace.enabled");

    private String propertyName;

    private GeneralPreferenceEnum(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
