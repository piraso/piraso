/*
 * Copyright (c) 2012. Piraso Alvin R. de Leon. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The Piraso licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ard.piraso.client.net;

import ard.piraso.api.JacksonUtils;
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
import org.codehaus.jackson.map.ObjectMapper;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static ard.piraso.api.PirasoConstants.*;

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

        this.mapper = JacksonUtils.createMapper();
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
        params.add(new BasicNameValuePair(SERVICE_PARAMETER, SERVICE_START_PARAMETER_VALUE));
        params.add(new BasicNameValuePair(PREFERENCES_PARAMETER, mapper.writeValueAsString(preferences)));

        if(watchedAddr != null) {
            params.add(new BasicNameValuePair(WATCHED_ADDR_PARAMETER, watchedAddr));
        }

        post.setEntity(new UrlEncodedFormEntity(params, ENCODING_UTF_8));

        HttpResponse response = client.execute(targetHost, post, context);
        StatusLine status = response.getStatusLine();

        if(status.getStatusCode() != HttpStatus.SC_OK) {
            throw new HttpPirasoException(status.toString());
        }

        responseEntity = response.getEntity();
        String contentType = responseEntity.getContentType().getValue().toLowerCase();

        if(!contentType.contains(XML_CONTENT_TYPE)) {
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
