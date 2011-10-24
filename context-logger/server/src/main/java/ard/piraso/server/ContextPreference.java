package ard.piraso.server;

/**
 * The current thread context preferences.
 */
public interface ContextPreference {

    /**
     * Determines whether the current context request is eligible for monitoring.
     *
     * @return {@code true} if eligible, {@code false} otherwise.
     */
    public boolean isMonitored();

    /**
     * Determines whether the given property is enabled or not.
     *
     * @param property the property to check.
     * @return {@code true} if enabled, {@code false} otherwise.
     */
    public boolean isEnabled(String property);

    /**
     * Determines the int property value.
     *
     * @param property the property to retrieve.
     * @return the property int value, {@code null} if not found.
     */
    public Integer getIntValue(String property);

    /**
     * When invoked, this determines that the current request is executed on a logging scoped.
     * <p>
     * For sql context logging, only request with transactional scope will trigger this. Any request that uses {@link javax.sql.DataSource} will
     * invoke this method.
     * <p>
     * This will ensure that request that only do resource serving will be ignored.
     */
    public void requestOnScope();
}
