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

import org.piraso.api.Preferences;
import org.piraso.api.entry.Entry;
import org.piraso.api.io.EntryReadAdapter;
import org.piraso.api.io.EntryReadEvent;
import org.piraso.api.io.EntryReadListener;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HttpContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.piraso.api.PirasoConstants.XML_CONTENT_TYPE;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test for {@link HttpPirasoEntryReader} class.
 */
public class HttpPirasoEntryReaderTest {

    private HttpClient client;

    private HttpPirasoEntryReader reader;

    private HttpPost capturedPost;

    private HttpResponse response;

    private HttpEntity entity;

    private Header contentTypeHeader;

    @Before
    public void setUp() throws Exception {
        HttpContext context = mock(HttpContext.class);
        client = mock(HttpClient.class);
        response = mock(HttpResponse.class);
        contentTypeHeader = mock(Header.class);
        entity = mock(HttpEntity.class);

        doReturn(entity).when(response).getEntity();
        doReturn(contentTypeHeader).when(entity).getContentType();

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                capturedPost = (HttpPost) invocationOnMock.getArguments()[1];

                return response;
            }
        }).when(client).execute(Matchers.<HttpHost>any(), Matchers.<HttpRequest>any(), Matchers.<HttpContext>any());

        reader = new HttpPirasoEntryReader(client, context);
        reader.setUri("http://localhost:8080/piraso/context/logging");
    }

    @Test
    public void testStartOnSuccess() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<piraso id=\"1\" watched-address=\"127.0.0.1\">\n" +
                "<entry class-name=\"org.piraso.api.entry.MessageEntry\" date=\"1319349832439\" id=\"1\">{\"message\":\"message\",\"elapseTime\":null}</entry>\n" +
                "</piraso>";

        Preferences preferences = new Preferences();

        StatusLine line = new BasicStatusLine(new ProtocolVersion("http", 1, 0), HttpStatus.SC_OK, "");
        doReturn(line).when(response).getStatusLine();

        doReturn(new ByteArrayInputStream(xml.getBytes())).when(entity).getContent();
        doReturn(XML_CONTENT_TYPE).when(contentTypeHeader).getValue();

        reader.getStartHandler().setPreferences(preferences);
        reader.getStartHandler().setWatchedAddr("127.0.0.1");

        final List<Entry> entries = new ArrayList<Entry>();

        reader.getStartHandler().addListener(new EntryReadAdapter() {
            @Override
            public void readEntry(EntryReadEvent evt) {
                entries.add(evt.getEntry());
            }
        });

        reader.start();

        assertNotNull(reader.getStartHandler().getWatchedAddr());
        assertNotNull(reader.getStartHandler().getId());
        assertTrue(reader.isComplete());

        assertEquals(1, CollectionUtils.size(entries));
        assertTrue(UrlEncodedFormEntity.class.isInstance(capturedPost.getEntity()));
        verify(client).execute(Matchers.<HttpHost>any(), Matchers.<HttpRequest>any(), Matchers.<HttpContext>any());

        reader.stop();

        // still once since already complete
        verify(client).execute(Matchers.<HttpHost>any(), Matchers.<HttpRequest>any(), Matchers.<HttpContext>any());
    }

    @Test(expected = HttpPirasoException.class)
    public void testStartInvalidStatusCode() throws Exception {
        Preferences preferences = new Preferences();

        StatusLine line = new BasicStatusLine(new ProtocolVersion("http", 1, 0), HttpStatus.SC_BAD_REQUEST, "Bad Request");
        doReturn(line).when(response).getStatusLine();

        reader.getStartHandler().setPreferences(preferences);
        reader.getStartHandler().setWatchedAddr("127.0.0.1");

        reader.start();
    }

    @Test(expected = HttpPirasoException.class)
    public void testStartInvalidContentType() throws Exception {
        Preferences preferences = new Preferences();

        StatusLine line = new BasicStatusLine(new ProtocolVersion("http", 1, 0), HttpStatus.SC_OK, "");
        doReturn(line).when(response).getStatusLine();
        doReturn("json/application").when(contentTypeHeader).getValue();

        reader.getStartHandler().setPreferences(preferences);

        reader.start();
    }

    @Test(expected = HttpPirasoException.class)
    public void testStopInvalidStatusCode() throws Exception {
        StatusLine line = new BasicStatusLine(new ProtocolVersion("http", 1, 0), HttpStatus.SC_BAD_REQUEST, "Bad Request");
        doReturn(line).when(response).getStatusLine();

        reader.stop();
    }

    @Test
    public void testEntryListener() throws Exception {
        EntryReadListener listener = mock(EntryReadListener.class);

        reader.getStartHandler().addListener(listener);
        assertEquals(1, reader.getStartHandler().getListeners().size());

        reader.getStartHandler().removeListener(listener);
        assertTrue(reader.getStartHandler().getListeners().isEmpty());

        reader.getStartHandler().addListener(listener);
        assertEquals(1, reader.getStartHandler().getListeners().size());

        reader.getStartHandler().clearListeners();
        assertTrue(reader.getStartHandler().getListeners().isEmpty());
    }

    @Test
    public void testStopSuccess() throws Exception {
        StatusLine line = new BasicStatusLine(new ProtocolVersion("http", 1, 0), HttpStatus.SC_OK, "");
        doReturn(line).when(response).getStatusLine();

        reader.stop();
    }
}
