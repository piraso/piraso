package ard.piraso.api.io;

import ard.piraso.api.entry.Entry;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
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
        mapper = new ObjectMapper();
        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);

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

        transformer.transform(source, outputTarget);

        StringBuffer buf = out.getBuffer();
        return buf.substring(buf.indexOf("<entry"));
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
