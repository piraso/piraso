package ard.piraso.client.net;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.URI;

/**
 * Reads entry from an http response.
 */
public class HttpPirasoEntryReader {
    private URI uri;

    private HttpHost targetHost;

    private HttpPirasoStartHandler startHandler;

    private HttpPirasoStopHandler stopHandler;

    public HttpPirasoEntryReader(AbstractHttpClient client, HttpContext context) {
        startHandler = new HttpPirasoStartHandler(client, context);
        stopHandler = new HttpPirasoStopHandler(client, context);
    }

    public void setUri(String uri) {
        this.uri = URI.create(uri);

        targetHost = new HttpHost(this.uri.getHost(), this.uri.getPort(), this.uri.getScheme());
    }

    public HttpPirasoStartHandler getStartHandler() {
        return startHandler;
    }

    public HttpPirasoStopHandler getStopHandler() {
        return stopHandler;
    }

    public void start() throws IOException {
        startHandler.setTargetHost(targetHost);
        startHandler.setUri(uri);
        startHandler.execute();
    }

    public void stop() throws IOException {
        stopHandler.setTargetHost(targetHost);
        stopHandler.setUri(uri);
        stopHandler.setId(startHandler.getId());
        stopHandler.execute();
    }

}
