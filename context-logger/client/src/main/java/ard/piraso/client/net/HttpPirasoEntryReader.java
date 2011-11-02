package ard.piraso.client.net;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.protocol.HttpContext;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
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

    public HttpPirasoEntryReader(HttpClient client, HttpContext context) {
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

    public void start() throws IOException, SAXException, ParserConfigurationException {
        startHandler.setTargetHost(targetHost);
        startHandler.setUri(uri);

        startHandler.execute();
    }

    public boolean isComplete() {
        return startHandler.isComplete();
    }

    public void stop() throws IOException {
        if(!startHandler.isComplete()) {
            stopHandler.setTargetHost(targetHost);
            stopHandler.setUri(uri);
            stopHandler.setId(startHandler.getId());

            stopHandler.execute();
        }
    }

}
