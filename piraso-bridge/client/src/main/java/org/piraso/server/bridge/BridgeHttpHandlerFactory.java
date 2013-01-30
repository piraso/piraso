package org.piraso.server.bridge;

import org.piraso.client.net.HttpPirasoTestHandler;
import org.piraso.server.bridge.net.HttpPirasoGetBridgeRegistryHandler;
import org.piraso.server.bridge.net.HttpPirasoIDRequestHandler;
import org.piraso.server.bridge.net.HttpPirasoLogHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BridgeHttpHandlerFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private HttpPirasoInitializer initializer;

    public void setInitializer(HttpPirasoInitializer initializer) {
        this.initializer = initializer;
    }

    public HttpPirasoGetBridgeRegistryHandler createGetRegistryHandler() {
        HttpPirasoGetBridgeRegistryHandler handler = applicationContext.getBean(HttpPirasoGetBridgeRegistryHandler.class);
        initializer.init(handler);

        return handler;
    }

    public HttpPirasoIDRequestHandler createIDRequestHandler() {
        HttpPirasoIDRequestHandler handler = applicationContext.getBean(HttpPirasoIDRequestHandler.class);
        initializer.init(handler);

        return handler;
    }

    public HttpPirasoLogHandler createLogHandler() {
        HttpPirasoLogHandler handler = applicationContext.getBean(HttpPirasoLogHandler.class);
        initializer.init(handler);

        return handler;
    }

    public HttpPirasoTestHandler createTestHandler() {
        HttpPirasoTestHandler handler = applicationContext.getBean(HttpPirasoTestHandler.class);
        initializer.init(handler);

        return handler;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
