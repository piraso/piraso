package org.piraso.server.service;

import java.util.ArrayList;
import java.util.List;

public class BridgeRegistry {

    private List<BridgeLogger> loggers;

    public BridgeRegistry() {
        loggers = new ArrayList<BridgeLogger>();
    }

    public void addLogger(BridgeLogger logger) {
        loggers.add(logger);
    }

    public List<BridgeLogger> getLoggers() {
        return loggers;
    }

    public void setLoggers(List<BridgeLogger> loggers) {
        this.loggers = loggers;
    }
}
