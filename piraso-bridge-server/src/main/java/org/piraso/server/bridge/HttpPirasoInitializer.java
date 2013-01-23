package org.piraso.server.bridge;

import org.piraso.client.net.AbstractHttpHandler;

public interface HttpPirasoInitializer {
    void init(AbstractHttpHandler handler);
}
