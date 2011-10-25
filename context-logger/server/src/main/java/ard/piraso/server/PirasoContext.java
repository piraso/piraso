package ard.piraso.server;

import ard.piraso.api.GeneralPreferenceEnum;
import ard.piraso.api.Preferences;
import ard.piraso.api.entry.Entry;
import ard.piraso.server.logger.TraceableID;
import ard.piraso.server.service.ResponseLoggerService;
import ard.piraso.server.service.UserRegistry;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents the current request context.
 */
public class PirasoContext implements ContextPreference {

    /**
     * final class logging instance.
     */
    private static final Log LOG = LogFactory.getLog(PirasoContext.class);

    private UserRegistry registry;

    private HttpServletRequest request;

    private boolean requestOnScope;

    private LinkedList<EntryHolder> entryQueue = new LinkedList<EntryHolder>();

    PirasoContext(HttpServletRequest request, UserRegistry registry) {
        this.registry = registry;
        this.request = request;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isMonitored() {
        try {
            return CollectionUtils.isNotEmpty(registry.getContextPreferences(request));
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEnabled(String property) {
        try {
            List<Preferences> preferencesList = registry.getContextPreferences(request);

            for(Preferences pref : preferencesList) {
                if(pref.isEnabled(property)) {
                    return true;
                }
            }

            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Integer getIntValue(String property) {
        try {
            List<Preferences> preferencesList = registry.getContextPreferences(request);
            Integer max = null;

            for(Preferences pref : preferencesList) {
                Integer intValue = pref.getIntValue(property);
                if(max == null || (intValue != null && intValue > max)) {
                    max = intValue;
                }
            }

            return max;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void requestOnScope() {
        requestOnScope = true;

        // ensure to log queued
        while(!entryQueue.isEmpty()) {
            EntryHolder holder = entryQueue.remove(0);
            logScoped(holder.id, holder.entry, true);
        }
    }

    private void logScoped(TraceableID id, Entry entry, boolean scopedEnabled) {
        try {
            List<ResponseLoggerService> loggers = registry.getContextLoggers(request);

            for(ResponseLoggerService logger : loggers) {
                Preferences preferences = logger.getPreferences();

                boolean actual = preferences.isEnabled(GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName());
                if(actual == scopedEnabled) {
                    logger.log(id, entry);
                }
            }
        } catch (IOException e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    /**
     * Log the given entry.
     *
     * @param preferenceProperty preference property
     * @param id the entry id
     * @param entry the entry to log
     */
    public void log(String preferenceProperty, TraceableID id, Entry entry) {
        if(!isMonitored()) {
            // ignore any logs when current context is not monitored.
            return;
        }

        // only log for those that ignores logging scope
        if(GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName().equals(preferenceProperty) && !requestOnScope) {
            logScoped(id, entry, false);

            if(isEnabled(GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName())) {
                entryQueue.add(new EntryHolder(id, entry));
            }

            return;
        }

        // when any other log is provided that is not scope aware then enable request for scope logging
        if(!requestOnScope && !GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName().equals(preferenceProperty)) {
            requestOnScope();
        }

        try {
            List<ResponseLoggerService> loggers = registry.getContextLoggers(request);

            for(ResponseLoggerService logger : loggers) {
                Preferences preferences = logger.getPreferences();

                if(preferenceProperty == null) {
                    logger.log(id, entry);
                } else if(preferences.isEnabled(preferenceProperty)) {
                    logger.log(id, entry);
                }
            }
        } catch (IOException e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    private class EntryHolder {
        private TraceableID id;

        private Entry entry;

        private EntryHolder(TraceableID id, Entry entry) {
            this.id = id;
            this.entry = entry;
        }
    }
}
