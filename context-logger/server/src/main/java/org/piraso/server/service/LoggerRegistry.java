package org.piraso.server.service;

import org.piraso.api.Preferences;
import org.piraso.server.PirasoEntryPoint;

import java.io.IOException;
import java.util.List;

public interface LoggerRegistry {
    /**
     * Retrieve all {@link org.piraso.api.Preferences} given the monitored address.
     *
     * @param entryPoint the http servlet request
     * @return list of {@link org.piraso.api.Preferences}
     * @throws java.io.IOException on io error
     */
    List<Preferences> getContextPreferences(PirasoEntryPoint entryPoint) throws IOException;

    /**
     * Retrieve all {@link ResponseLoggerService} given the monitored address.
     *
     * @param request the http servlet request
     * @return list of {@link ResponseLoggerService}
     * @throws IOException on io error
     */
    List<ResponseLoggerService> getContextLoggers(PirasoEntryPoint request) throws IOException;

}
