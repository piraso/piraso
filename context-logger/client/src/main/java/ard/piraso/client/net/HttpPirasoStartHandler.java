package ard.piraso.client.net;

import ard.piraso.api.Preferences;
import ard.piraso.api.io.EntryReadListener;
import ard.piraso.api.io.PirasoEntryReader;
import org.apache.commons.lang.Validate;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Piraso start handler
 */
public class HttpPirasoStartHandler extends AbstractHttpHandler {

    private Preferences preferences;

    private String watchedAddr;

    private ObjectMapper mapper;

    private PirasoEntryReader reader;

    private boolean complete;

    private HttpEntity responseEntity;

    private List<EntryReadListener> listeners = Collections.synchronizedList(new LinkedList<EntryReadListener>());

    public HttpPirasoStartHandler(HttpClient client, HttpContext context) {
        super(client, context);

        this.mapper = new ObjectMapper();
        this.mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public void setWatchedAddr(String watchedAddr) {
        this.watchedAddr = watchedAddr;
    }

    public String getWatchedAddr() {
        return reader.getWatchedAddr();
    }

    public void execute() throws IOException, SAXException, ParserConfigurationException {
        try {
            doExecute();
        } finally {
            complete = true;
            EntityUtils.consume(responseEntity);
        }
    }

    private void doExecute() throws IOException, SAXException, ParserConfigurationException {
        Validate.notNull(uri, "uri should not be null.");
        Validate.notNull(preferences, "preferences should not be null.");

        HttpPost post = new HttpPost(uri.getPath());

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("service", "start"));
        params.add(new BasicNameValuePair("preferences", mapper.writeValueAsString(preferences)));

        if(watchedAddr != null) {
            params.add(new BasicNameValuePair("watchedAddr", watchedAddr));
        }

        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = client.execute(targetHost, post, context);
        StatusLine status = response.getStatusLine();

        if(status.getStatusCode() != HttpStatus.SC_OK) {
            throw new HttpPirasoException(status.toString());
        }

        responseEntity = response.getEntity();
        String contentType = responseEntity.getContentType().getValue().toLowerCase();

        if(!contentType.contains("xml/plain")) {
            throw new HttpPirasoException("Invalid response content type: " + responseEntity.getContentType());
        }

        reader = new PirasoEntryReader(responseEntity.getContent());

        for(EntryReadListener listener : listeners) {
            reader.addListener(listener);
        }

        reader.start();
    }

    public boolean isComplete() {
        return complete;
    }

    public String getId() {
        Validate.notNull("#start() should be invoked before retrieving the id.");

        if(reader == null) {
            return null;
        } else {
            return reader.getId();
        }
    }

    public List<EntryReadListener> getListeners() {
        return listeners;
    }

    public void addListener(EntryReadListener listener) {
        listeners.add(listener);
    }

    public void removeListener(EntryReadListener listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }
}
