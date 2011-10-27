package ard.piraso.api.io;

import ard.piraso.api.entry.Entry;

import java.util.Date;
import java.util.EventObject;

/**
 * Defines a read entry event.
 */
public class EntryReadEvent<T> extends EventObject {

    private Entry entry;

    private Long requestId;

    private Date date;

    public EntryReadEvent(Object o, Long requestId, Entry entry, Date date) {
        super(o);

        this.requestId = requestId;
        this.entry = entry;
        this.date = date;
    }

    public Entry getEntry() {
        return entry;
    }

    public Long getRequestId() {
        return requestId;
    }

    public Date getDate() {
        return date;
    }
}
