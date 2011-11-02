package ard.piraso.client.net;

import org.apache.commons.lang.Validate;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Piraso stop handler
 */
public class HttpPirasoStopHandler extends AbstractHttpHandler {

    protected String id;

    public HttpPirasoStopHandler(HttpClient client, HttpContext context) {
        super(client, context);
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void execute() throws IOException {
        Validate.notNull(uri, "uri should not be null.");

        HttpPost post = new HttpPost(uri.getPath());

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("service", "start"));
        params.add(new BasicNameValuePair("activity_uuid", id));

        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = client.execute(targetHost, post, context);
        StatusLine status = response.getStatusLine();

        if(status.getStatusCode() != HttpStatus.SC_OK) {
            throw new HttpPirasoException(status.toString());
        }
    }
}
