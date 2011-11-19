package ard.piraso.api.io;

import ard.piraso.api.entry.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Register a {@link ard.piraso.api.entry.Entry} loader
 */
public final class PirasoEntryLoaderRegistry {

    private static final Log LOG = LogFactory.getLog(PirasoEntryLoaderRegistry.class);

    public static final PirasoEntryLoaderRegistry INSTANCE = new PirasoEntryLoaderRegistry();

    static {
        INSTANCE.addEntryLoader(new BasePirasoEntryLoader());
    }

    private List<PirasoEntryLoader> loaders = new LinkedList<PirasoEntryLoader>();

    public void addEntryLoader(PirasoEntryLoader loader) {
        loaders.add(loader);
    }

    public Entry loadEntry(String className, String content) {
        for(PirasoEntryLoader loader : loaders) {
            try {
                return loader.loadEntry(className, content);
            } catch(Exception e) {
                LOG.warn(e.getMessage(), e);
            }
        }

        throw new IllegalStateException(String.format("No loader found to load class %s", className));
    }
}
