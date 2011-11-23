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
 * Unless required by applicable law or agreed
 * to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ard.piraso.api.io;

import ard.piraso.api.JacksonUtils;
import ard.piraso.api.entry.Entry;
import org.codehaus.jackson.map.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Date;

/**
 * Piraso entry stream writer.
 */
public class PirasoEntryWriter implements Closeable {

    private ObjectMapper mapper;

    private PrintWriter writer;

    private DocumentBuilder builder;

    private Transformer transformer;

    public PirasoEntryWriter(String id, String watchedAddr, PrintWriter writer) throws ParserConfigurationException, TransformerConfigurationException {
        this.writer = writer;
        mapper = JacksonUtils.createMapper();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        builder = factory.newDocumentBuilder();
        transformer = transformerFactory.newTransformer();

        init(id, watchedAddr);
    }

    private void init(String id, String watchedAddr) {
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println(String.format("<piraso id=\"%s\" watched-address=\"%s\">", id, watchedAddr));
        writer.flush();
    }

    private String createXMLString(Date date, Entry entry) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        StringReader reader = new StringReader(String.format("<entry id=\"%d\" date=\"%s\" class-name=\"%s\"></entry>",
                entry.getRequestId(), mapper.writeValueAsString(date), entry.getClass().getName()));

        Document document = builder.parse(new InputSource(reader));

        NodeList nodeList = document.getElementsByTagName("entry");
        Element node = (Element) nodeList.item(0);
        node.appendChild(document.createTextNode(mapper.writeValueAsString(entry)));

        StringWriter out = new StringWriter();
        StreamResult outputTarget = new StreamResult(out);
        DOMSource source = new DOMSource(document);

        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(source, outputTarget);

        StringBuffer buf = out.getBuffer();
        return buf.toString();
    }

    public void write(Date date, Entry entry) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        writer.println(createXMLString(date, entry));
        writer.flush();
    }
    public void write(Entry entry) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        write(new Date(), entry);
    }

    public void close() {
        writer.write("</piraso>");
        writer.close();
    }
}
