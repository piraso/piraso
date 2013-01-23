package org.piraso.server.bridge;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.piraso.api.Preferences;
import org.piraso.server.PirasoEntryPoint;
import org.piraso.server.bridge.net.HttpPirasoGetBridgeRegistryHandler;
import org.piraso.server.service.*;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.util.List;

public class BridgeLoggerRegistryImpl implements LoggerRegistry {

    private static final Log LOG = LogFactory.getLog(BridgeLoggerRegistryImpl.class);

    private DefaultUserRegistryImpl registry = new DefaultUserRegistryImpl();

    private BridgeHttpHandlerFactory factory;

    @Required
    public void setFactory(BridgeHttpHandlerFactory factory) {
        this.factory = factory;
    }

    public void init() {
        try {
            HttpPirasoGetBridgeRegistryHandler handler = factory.createGetRegistryHandler();
            registry.clear();

            BridgeRegistry bridgeRegistry = handler.getRegistry();
            for(BridgeLogger logger : bridgeRegistry.getLoggers()) {
                registry.associate(logger.getUser(), new BridgeLoggerServiceImpl(logger, factory));
            }
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    public void refreshLoggers() {
        if(BridgeConfig.INSTANCE.isQueryAlways()) {
            init();
        }
    }

    public List<Preferences> getContextPreferences(PirasoEntryPoint entryPoint) throws IOException {
        refreshLoggers();

        return registry.getContextPreferences(entryPoint);
    }

    public List<ResponseLoggerService> getContextLoggers(PirasoEntryPoint request) throws IOException {
        refreshLoggers();

        return registry.getContextLoggers(request);
    }
}
