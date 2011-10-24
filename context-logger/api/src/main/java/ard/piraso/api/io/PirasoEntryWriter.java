package ard.piraso.api.io;

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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

/**
 * Piraso entry stream writer.
 */
public class PirasoEntryWriter {

    private ObjectMapper mapper = new ObjectMapper();

    private PrintWriter writer;

    private DocumentBuilder builder;

    private Transformer transformer;

    public PirasoEntryWriter(long id, PrintWriter writer) throws ParserConfigurationException, TransformerConfigurationException {
        this.writer = writer;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        builder = factory.newDocumentBuilder();
        transformer = transformerFactory.newTransformer();

        init(id);
    }

    private void init(long id) {
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println(String.format("<piraso id=\"%d\">", id));
        writer.flush();
    }

    private String createXMLString(String id, Date date, Entry entry) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        StringReader reader = new StringReader(String.format("<entry id=\"%s\" date=\"%s\" className=\"%s\"></entry>",
                id, mapper.writeValueAsString(date), entry.getClass().getName()));

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

    public void write(String id, Entry entry) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        writer.println(createXMLString(id, new Date(), entry));
        writer.flush();
    }

    public void close() {
        writer.write("</piraso>");
        writer.close();
    }
}
