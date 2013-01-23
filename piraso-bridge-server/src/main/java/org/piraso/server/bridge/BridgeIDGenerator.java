package org.piraso.server.bridge;

import org.piraso.api.NextGenerator;
import org.piraso.server.bridge.net.HttpPirasoIDRequestHandler;
import org.springframework.beans.factory.annotation.Required;

public class BridgeIDGenerator implements NextGenerator<Long> {

    private BridgeHttpHandlerFactory factory;

    @Required
    public void setFactory(BridgeHttpHandlerFactory factory) {
        this.factory = factory;
    }

    public Long next() {
        HttpPirasoIDRequestHandler handler = factory.createIDRequestHandler();

        try {
            handler.execute();

            return handler.getRequestID();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
