package ard.piraso.server;

/**
 * Created by IntelliJ IDEA.
 * User: adleon
 * Date: 10/19/11
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ContextPreference {

    public boolean isMonitored();

    public boolean isEnabled(String property);

    public Integer getIntValue(String property);
}
