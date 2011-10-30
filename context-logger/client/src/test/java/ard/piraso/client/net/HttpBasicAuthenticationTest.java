package ard.piraso.client.net;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;
import org.mockito.Matchers;

import java.net.URI;

import static org.mockito.Mockito.*;

/**
 * Test for {@link HttpBasicAuthentication} class.
 */
public class HttpBasicAuthenticationTest {
    @Test
    public void testAuthenticate() throws Exception {
        AbstractHttpClient client = mock(AbstractHttpClient.class);
        HttpContext context = mock(HttpContext.class);
        CredentialsProvider credentials = mock(CredentialsProvider.class);

        doReturn(credentials).when(client).getCredentialsProvider();

        HttpBasicAuthentication auth = new HttpBasicAuthentication(client, context);
        auth.setUserName("username");
        auth.setPassword("password");

        URI uri = URI.create("http://localhost:8080/test");

        auth.setTargetHost(new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme()));

        auth.execute();

        verify(credentials).setCredentials(Matchers.<AuthScope>any(), Matchers.<Credentials>any());
        verify(context).setAttribute(Matchers.<String>any(), any());
    }
}
