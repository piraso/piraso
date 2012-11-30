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
