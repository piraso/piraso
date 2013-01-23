package org.piraso.server.bridge.net;

import org.apache.commons.lang.Validate;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.piraso.api.JacksonUtils;
import org.piraso.api.entry.Entry;
import org.piraso.client.net.AbstractHttpHandler;
import org.piraso.client.net.HttpPirasoException;
import org.piraso.server.service.User;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.piraso.api.PirasoConstants.*;
import static org.piraso.api.PirasoConstants.ENCODING_UTF_8;

public class HttpPirasoLogHandler extends AbstractHttpHandler {

    private Entry entry;

    private User user;

    private ObjectMapper mapper;

    private HttpEntity responseEntity;

    public HttpPirasoLogHandler(HttpClient client, HttpContext context) {
        super(client, context);

        mapper = JacksonUtils.MAPPER;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public void setUser(User user) {
        this.user = user;
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
        Validate.notNull(entry, "entry should not be null.");
        Validate.notNull(user, "user should not be null.");

        HttpPost post = new HttpPost(uri.getPath());

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(SERVICE_PARAMETER, SERVICE_LOG_PARAMETER_VALUE));
        params.add(new BasicNameValuePair(ENTRY_CLASS_NAME_PARAMETER, entry.getClass().getName()));
        params.add(new BasicNameValuePair(ENTRY_REQUEST_ID_PARAMETER, String.valueOf(entry.getRequestId())));
        params.add(new BasicNameValuePair(ENTRY_PARAMETER, mapper.writeValueAsString(entry)));
        params.add(new BasicNameValuePair(USER_PARAMETER, mapper.writeValueAsString(user)));

        post.setEntity(new UrlEncodedFormEntity(params, ENCODING_UTF_8));

        HttpResponse response = client.execute(targetHost, post, context);
        StatusLine status = response.getStatusLine();

        if(status.getStatusCode() != HttpStatus.SC_OK) {
            throw new HttpPirasoException(status.toString());
        }

        responseEntity = response.getEntity();
    }
}
