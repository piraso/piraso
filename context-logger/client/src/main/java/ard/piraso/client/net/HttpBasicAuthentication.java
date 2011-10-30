package ard.piraso.client.net;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.HttpContext;

/**
 * HTTP basic authentication
 */
public class HttpBasicAuthentication extends AbstractHttpAuthentication {

    public HttpBasicAuthentication(AbstractHttpClient client, HttpContext context) {
        super(client, context);
    }

    public void authenticate() {
        validate();

        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);

        client.getCredentialsProvider().setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()), credentials);

        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();

        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

        context.setAttribute(ClientContext.AUTH_CACHE, authCache);
    }
}
