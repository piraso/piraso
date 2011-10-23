package ard.piraso.server.service;

import ard.piraso.api.IDGenerator;
import ard.piraso.api.Preferences;
import ard.piraso.api.entry.Entry;
import ard.piraso.api.io.PirasoEntryWriter;
import ard.piraso.server.logger.TraceableID;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Response logger service implementation.
 */
public class ResponseLoggerServiceImpl implements ResponseLoggerService {

    private static final Log LOG = LogFactory.getLog(ResponseLoggerServiceImpl.class);

    private static final int MAX_QUEUE_FORCE_KILL_SIZE = 1000;

    private static final IDGenerator ID_GENERATOR = new IDGenerator();

    private static final String MONITORED_ADDR_PARAMETER = "monitoredAddr";

    private static final String PREFERENCES_PARAMETER = "preferences";

    private static final String RESPONSE_CONTENT_TYPE = "xml/plain";

    private List<TransferEntryHolder> transferQueue = Collections.synchronizedList(new LinkedList<TransferEntryHolder>());

    private long id = ID_GENERATOR.next();

    private User user;

    private String monitoredAddr;

    private HttpServletResponse response;

    private PirasoEntryWriter writer;

    private boolean alive = true;

    private Preferences preferences;

    public ResponseLoggerServiceImpl(User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Validate.notNull(request.getParameter(MONITORED_ADDR_PARAMETER), String.format("Request parameter '%s' is required", MONITORED_ADDR_PARAMETER));
        Validate.notNull(request.getParameter(PREFERENCES_PARAMETER), String.format("Request parameter '%s' is required", PREFERENCES_PARAMETER));

        this.user = user;
        this.response = response;
        this.monitoredAddr = request.getParameter(MONITORED_ADDR_PARAMETER);
        this.preferences = new ObjectMapper().readValue(request.getParameter(PREFERENCES_PARAMETER), Preferences.class);
    }

    /**
     * {@inheritDoc}
     */
    public User getUser() {
        return user;
    }

    /**
     * {@inheritDoc}
     */
    public String getMonitoredAddr() {
        return monitoredAddr;
    }

    /**
     * {@inheritDoc}
     */
    public long getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isAlive() throws IOException {
        return alive;
    }

    /**
     * {@inheritDoc}
     */
    public void start() throws IOException, TransformerConfigurationException, ParserConfigurationException {
        response.setContentType(RESPONSE_CONTENT_TYPE);

        try {
            writer = new PirasoEntryWriter(getId(), response.getWriter());
            doLogWhileAlive();
        } finally {
            synchronized (this) {
                if(writer != null) {
                    writer.close();
                }

                notifyAll();
            }
        }
    }

    private void waitTillNoEntry() {
        if(transferQueue.isEmpty()) {
            try {
                wait(1800000l);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void writeAllTransferFromQueue() {
        while(!transferQueue.isEmpty()) {
            try {
                TransferEntryHolder holder = transferQueue.remove(0);

                writer.write(holder.id.toString(), holder.entry);
            } catch (Exception e) {
                LOG.warn(e.getMessage(), e);
            }
        }
    }

    private void doLogWhileAlive() {
        synchronized (this) {
            while(alive) {
                waitTillNoEntry();
                writeAllTransferFromQueue();
            }
        }
    }

    public void stop() throws IOException {
        synchronized(this) {
            alive = false;
            notifyAll();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void stopAndWait(long timeout) throws InterruptedException {
        synchronized(this) {
            if(alive) {
                wait(timeout);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public Preferences getPreferences() {
        return preferences;
    }

    /**
     * {@inheritDoc}
     */
    public void log(TraceableID id, Entry entry) throws IOException {
        synchronized (this) {
            if(transferQueue.size() > MAX_QUEUE_FORCE_KILL_SIZE) {
                stop();

                return;
            }

            transferQueue.add(new TransferEntryHolder(id, entry));
            notifyAll();
        }
    }
}
