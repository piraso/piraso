package org.piraso.server.bridge;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.piraso.api.GeneralPreferenceEnum;
import org.piraso.api.JacksonUtils;
import org.piraso.api.Preferences;
import org.piraso.api.entry.Entry;
import org.piraso.server.bridge.net.HttpPirasoLogHandler;
import org.piraso.server.service.*;

import java.io.IOException;
import java.io.StringWriter;

public class BridgeLoggerServiceImpl implements ResponseLoggerService {

    private static final Log LOG = LogFactory.getLog(BridgeLoggerServiceImpl.class);

    private BridgeLogger logger;

    private ObjectMapper mapper;

    private BridgeHttpHandlerFactory factory;

    public BridgeLoggerServiceImpl(BridgeLogger logger, BridgeHttpHandlerFactory factory) {
        this.logger = logger;
        this.factory = factory;
        mapper = JacksonUtils.MAPPER;
    }

    public User getUser() {
        return logger.getUser();
    }

    public String getId() {
        return logger.getUser().getActivityUuid();
    }

    public Long getGlobalId() {
        return logger.getGlobalId();
    }

    public String getWatchedAddr() {
        return logger.getWatchedAddr();
    }

    public boolean isWatched(String remoteAddr) {
        return logger.isWatched(remoteAddr);
    }

    public Preferences getPreferences() {
        return logger.getPreferences();
    }

    public void start() throws Exception {
    }

    public void stop() throws IOException {
    }

    public boolean isAlive() {
        return true;
    }

    public boolean isForcedStopped() {
        return false;
    }

    public void stopAndWait(long timeout) throws InterruptedException, IOException {
    }

    public void log(Entry entry) throws IOException {
        if(getPreferences().isEnabled(GeneralPreferenceEnum.NO_REQUEST_CONTEXT.getPropertyName())) {
            StringWriter writer = new StringWriter();
            mapper.writeValue(writer, entry);
            entry = mapper.readValue(writer.toString(), entry.getClass());

            entry.setRequestId(getGlobalId());
        }

        try {
            HttpPirasoLogHandler handler = factory.createLogHandler();
            handler.setEntry(entry);
            handler.setUser(getUser());
            handler.execute();
        } catch(Exception e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    public void addStopListener(StopLoggerListener listener) {
    }

    public void removeStopListener(StopLoggerListener listener) {
    }

    public void fireStopEvent(StopLoggerEvent event) {
    }
}
