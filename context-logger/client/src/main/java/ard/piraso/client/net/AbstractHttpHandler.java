package ard.piraso.client.net;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.protocol.HttpContext;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URI;

/**
 * Base class for base handler sub classes.
 */
public abstract class AbstractHttpHandler {
    protected HttpClient client;

    protected HttpContext context;

    protected HttpHost targetHost;

    protected URI uri;

    public AbstractHttpHandler(HttpClient client, HttpContext context) {
        this.client = client;
        this.context = context;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void setTargetHost(HttpHost targetHost) {
        this.targetHost = targetHost;
    }

    public abstract void execute() throws IOException, SAXException, ParserConfigurationException;
}
