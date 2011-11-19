package ard.piraso.server;

import ard.piraso.api.Preferences;

/**
 * Defines an interface of a request.
 */
public interface PirasoRequest {

    Preferences getPreferences();

    String getRemoteAddr();

    String getWatchedAddr();

    String getActivityUuid();
}
