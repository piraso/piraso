package ard.piraso.api;

/**
 * General preference enumeration.
 */
public enum GeneralPreferenceEnum {
    /**
     *  property name for enabling stack trace.
     */
    STACK_TRACE_ENABLED("general.stack.trace.enabled");

    /**
     * the preference property name.
     */
    private final String propertyName;

    /**
     * Construct enum given the enum property name.
     *
     * @param newPropertyName  the property name
     */
    private GeneralPreferenceEnum(final String newPropertyName) {
        this.propertyName = newPropertyName;
    }

    /**
     * Getter method for {@link #propertyName} property.
     *
     * @return the property value
     */
    public String getPropertyName() {
        return propertyName;
    }
}
