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

    private HttpPirasoTestHandler testHandler;

    public HttpPirasoEntryReader(HttpClient client, HttpContext context) {
        startHandler = new HttpPirasoStartHandler(client, context);
        stopHandler = new HttpPirasoStopHandler(client, context);
        testHandler = new HttpPirasoTestHandler(client, context);
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

    public boolean testConnection() throws Exception {
        testHandler.setUri(uri);
        testHandler.execute();

        return testHandler.isSuccess();
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
