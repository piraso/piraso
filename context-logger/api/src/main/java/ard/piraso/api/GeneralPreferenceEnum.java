package ard.piraso.api;

/**
 * General preference enumeration.
 */
public enum GeneralPreferenceEnum {
    /**
     *  property name for enabling stack trace.
     */
    STACK_TRACE_ENABLED("general.stack.trace.enabled"),

    /**
     * property name for scoped enabled. This means that only monitor request under logging scoped.
     * <p>
     * Example: for sql logging, only when the request requires {@link javax.sql.DataSource} will be monitored.
     * Any other request like, resources, will not result to any logs.
     */
    SCOPE_ENABLED("general.scoped.enabled");

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
