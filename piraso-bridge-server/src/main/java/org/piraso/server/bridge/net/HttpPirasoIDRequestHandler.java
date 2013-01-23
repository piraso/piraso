package org.piraso.server.bridge.net;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.piraso.client.net.AbstractHttpHandler;
import org.piraso.client.net.HttpPirasoException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.piraso.api.PirasoConstants.*;

public class HttpPirasoIDRequestHandler extends AbstractHttpHandler {
    private HttpEntity responseEntity;

    private Long requestID;

    public HttpPirasoIDRequestHandler(HttpClient client, HttpContext context) {
        super(client, context);
    }

    @Override
    public void execute() throws IOException, SAXException, ParserConfigurationException {
        try {
            doExecute();
        } finally {
            EntityUtils.consume(responseEntity);
        }
    }

    private void doExecute() throws IOException, SAXException, ParserConfigurationException {
        Validate.notNull(uri, "uri should not be null.");

        HttpPost post = new HttpPost(uri.getPath());

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(SERVICE_PARAMETER, SERVICE_REQUEST_ID_PARAMETER_VALUE));

        post.setEntity(new UrlEncodedFormEntity(params, ENCODING_UTF_8));

        HttpResponse response = client.execute(targetHost, post, context);
        StatusLine status = response.getStatusLine();

        if(status.getStatusCode() != HttpStatus.SC_OK) {
            throw new HttpPirasoException(status.toString());
        }

        responseEntity = response.getEntity();
        String contentType = responseEntity.getContentType().getValue().toLowerCase();

        if(!contentType.contains(PLAIN_CONTENT_TYPE)) {
            throw new HttpPirasoException("Invalid response content type: " + responseEntity.getContentType());
        }

        this.requestID = Long.valueOf(IOUtils.toString(responseEntity.getContent()));
    }

    public Long getRequestID() {
        return requestID;
    }
}
