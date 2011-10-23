package ard.piraso.server.service;

import ard.piraso.api.entry.Entry;
import ard.piraso.server.logger.TraceableID;

/**
 * Holds the {@link TraceableID} and {@link Entry} instances for transfer.
 */
class TransferEntryHolder {

    protected TraceableID id;

    protected Entry entry;

    TransferEntryHolder(TraceableID id, Entry entry) {
        this.id = id;
        this.entry = entry;
    }
}
