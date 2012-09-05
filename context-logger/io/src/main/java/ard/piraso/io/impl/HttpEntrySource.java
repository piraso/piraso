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

package ard.piraso.io.impl;

import ard.piraso.api.Preferences;
import ard.piraso.api.io.EntryReadListener;
import ard.piraso.client.net.HttpPirasoEntryReader;
import ard.piraso.io.IOEntrySource;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParamBean;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 * @author adleon
 */
public class HttpEntrySource implements IOEntrySource {
    
    private static final Logger LOG = Logger.getLogger(HttpEntrySource.class.getName());

    private HttpPirasoEntryReader reader;

    private boolean alive;

    private Preferences preferences;

    private String uri;

    private String watchedAddr;

    private String name;

    public HttpEntrySource(Preferences preferences, String uri) {
        this(preferences, uri, null);
    }
    
    public HttpEntrySource(Preferences preferences, String uri, String watchedAddr) {
        this.preferences = preferences;
        this.uri = uri;
        this.watchedAddr = watchedAddr;
    }

    public IOEntrySource createNew() {
        HttpEntrySource source =  new HttpEntrySource(preferences, uri, watchedAddr);
        source.setName(name);

        return source;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void initReader() {
        alive = false;

        ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager();
        manager.setDefaultMaxPerRoute(2);
        manager.setMaxTotal(2);

        HttpParams params = new BasicHttpParams();

        // set timeout
        HttpConnectionParamBean connParamBean = new HttpConnectionParamBean(params);
        connParamBean.setConnectionTimeout(3000);
        connParamBean.setSoTimeout(1000 * 60 * 120);

        HttpClient client = new DefaultHttpClient(manager, params);
        HttpContext context = new BasicHttpContext();

        this.reader = new HttpPirasoEntryReader(client, context);

        reader.setUri(uri);
        reader.getStartHandler().setPreferences(preferences);

        if(watchedAddr != null) {
            reader.getStartHandler().setWatchedAddr(watchedAddr);
        }
    }

    public void reset() {
        initReader();
    }

    public boolean testConnection() throws Exception {
        if(reader == null && !alive) {
            initReader();
        }

        return reader != null && reader.testConnection();
    }

    public String getName() {
        return name;
    }

    public void start() {
        try {
            if(reader == null && !alive) {
                initReader();
            }

            if(!alive) {
                LOG.info("Starting Context Monitor for URL : " + uri);
                alive = true;
                reader.start();
            }
        } catch (Exception ex) {
            LOG.warning(ex.getMessage());
            ex.printStackTrace();
        } finally {
            LOG.info("Stopped Context Monitor for URL : " + uri);
            alive = false;
        }
    }

    public void stop() {
        try {
            if(alive) {
                LOG.info("Stopping Context Monitor for URL : " + uri);
                reader.stop();
            } else {
                LOG.info("Not stopped since already not active URL : " + uri);
            }
        } catch (IOException ex) {
            LOG.warning(ex.getMessage());
            ex.printStackTrace();
        } finally {
             alive = false;
        }
    }

    public String getId() {
        return reader.getStartHandler().getId();
    }

    public boolean isAlive() {
        return alive;
    }

    public void addListener(EntryReadListener listener) {
        reader.getStartHandler().addListener(listener);
    }

    public void removeListener(EntryReadListener listener) {
        reader.getStartHandler().removeListener(listener);
    }

    public boolean isRestartable() {
        return true;
    }

    public String getWatchedAddr() {
        if(reader == null) {
            return null;
        }

        return reader.getStartHandler().getWatchedAddr();
    }
}
