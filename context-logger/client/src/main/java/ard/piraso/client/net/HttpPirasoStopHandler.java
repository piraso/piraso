/*
 * Copyright (c) 2011. Piraso Alvin R. de Leon. All Rights Reserved.
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
