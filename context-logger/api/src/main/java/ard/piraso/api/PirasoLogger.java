package ard.piraso.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Piraso based loggers
 */
public final class PirasoLogger {

    private static final Log LOG_PROXY_ENTRY = LogFactory.getLog("piraso.proxy.entry");

    private static final Log LOG_USER_REGISTRY = LogFactory.getLog("piraso.user.registry");

    private static final Log LOG_ENTRY_POINT = LogFactory.getLog("piraso.context.entry.point");

    public static Log getProxyEntry() {
        return LOG_PROXY_ENTRY;
    }

    public static Log getUserRegistry() {
        return LOG_USER_REGISTRY;
    }

    public static Log getEntryPoint() {
        return LOG_ENTRY_POINT;
    }
}
