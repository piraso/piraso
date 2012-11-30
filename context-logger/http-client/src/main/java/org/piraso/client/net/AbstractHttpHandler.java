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

package org.piraso.client.net;

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
