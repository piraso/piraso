package ard.piraso.api.io;

import ard.piraso.api.entry.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Piraso entry reader.
 */
public class PirasoEntryReader extends DefaultHandler {

    private static final Log LOG = LogFactory.getLog(PirasoEntryReader.class);

    private List<EntryReadListener> listeners = Collections.synchronizedList(new LinkedList<EntryReadListener>());

    private StringBuilder content = new StringBuilder();

    private ObjectMapper mapper = new ObjectMapper();

    private String id;

    private String monitor;

    private String currentEntryId;

    private Date currentEntryDate;

    private String currentEntryClassName;

    private InputStream in;

    public PirasoEntryReader(InputStream in) {
        this.in = in;
    }

    public void start() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        parser.parse(in, this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals("entry")) {
            try {
                if(currentEntryClassName != null) {
                    Class clazz = Class.forName(currentEntryClassName);
                    Entry entry = (Entry) mapper.readValue(content.toString(), clazz);

                    fireEntryReadEvent(new EntryReadEvent(this, currentEntryId, entry, currentEntryDate));
                } else {
                    LOG.warn(String.format("Unable to parse entry with value '%s'", content));
                }
            } catch (Exception e) {
                LOG.warn(String.format("Unable to parse entry with class name '%s' and value '%s'", currentEntryClassName, content), e);
            }
        }

        content.delete(0, content.length());
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals("piraso")) {
            id = attributes.getValue("id");
            monitor = attributes.getValue("monitor");
        } else if(qName.equals("entry")) {
            try {
                currentEntryClassName = attributes.getValue("className");
                currentEntryDate = mapper.readValue(attributes.getValue("date"), Date.class);
                currentEntryId = attributes.getValue("id");
            } catch (IOException e) {
                LOG.warn(String.format("Unable to parse entry with attributes '%s'", attributes.toString()));

                currentEntryClassName = null;
                currentEntryDate = null;
                currentEntryId = null;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        content.append(new String(ch, start, length));
    }

    public String getId() {
        return id;
    }

    public String getMonitor() {
        return monitor;
    }

    public void fireEntryReadEvent(EntryReadEvent evt) {
        List<EntryReadListener> tmp = new ArrayList<EntryReadListener>(listeners);
        for(EntryReadListener listener : tmp) {
            listener.readEntry(evt);
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
