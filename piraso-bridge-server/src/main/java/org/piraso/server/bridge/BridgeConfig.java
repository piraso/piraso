package org.piraso.server.bridge;

import org.piraso.client.net.AbstractHttpHandler;
import org.piraso.client.net.HttpPirasoTestHandler;
import org.springframework.beans.factory.annotation.Required;

import java.net.URI;

public enum BridgeConfig implements HttpPirasoInitializer {
    INSTANCE;

    private static final String PIRASO_BRIDGE_URL_PROPERTY = "piraso.bridge.url";

    private static final String PIRASO_BRIDGE_ID_PROPERTY = "piraso.bridge.id";

    private static final String PIRASO_BRIDGE_ENV = "PIRASO_URL";

    private static final URI DEFAULT_URI = URI.create("http://127.0.0.1:143442/piraso/logging");

    private BridgeHttpHandlerFactory factory;

    private Boolean enabled;

    private URI bridgeURI;

    @Required
    public void setFactory(BridgeHttpHandlerFactory factory) {
        factory.setInitializer(this);
        this.factory = factory;
    }

    public synchronized void init() {
        String url = System.getProperty(PIRASO_BRIDGE_URL_PROPERTY);
        if(url == null) {
            url = System.getenv(PIRASO_BRIDGE_ENV);
        }

        if(url != null) {
            bridgeURI = URI.create(url);
            if(isBridgeSupported()) {
                return;
            }
        }

        bridgeURI = DEFAULT_URI;
        enabled = isBridgeSupported();
    }

    private boolean isBridgeSupported() {
        try {
            HttpPirasoTestHandler handler = factory.createTestHandler();
            handler.execute();

            return handler.isBridgetSupported();
        } catch(Exception e) {
            return false;
        }
    }

    public void init(AbstractHttpHandler handler) {
        handler.setUri(bridgeURI);
    }

    public boolean getLoggingEnabled() {
        if(enabled == null) {
            return false;
        }

        return enabled;
    }

    public String getIdentifier() {
        return System.getProperty(PIRASO_BRIDGE_ID_PROPERTY);
    }

    public boolean isQueryAlways() {
        return false;
    }
}
