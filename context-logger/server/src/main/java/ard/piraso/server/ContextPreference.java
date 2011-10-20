package ard.piraso.server;

/**
 * The current thread context preferences.
 */
public interface ContextPreference {

    public boolean isMonitored();

    public boolean isEnabled(String property);

    public Integer getIntValue(String property);
}
