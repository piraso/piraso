package ard.piraso.server;

/**
 * Http request context preference.
 */
public class RequestContextPreference implements ContextPreference {

    public boolean isMonitored() {
        return false;
    }

    public boolean isEnabled(String property) {
        return false;
    }

    public Integer getIntValue(String property) {
        return null;
    }
}
