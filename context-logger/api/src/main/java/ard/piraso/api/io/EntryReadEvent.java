package ard.piraso.api.io;

import ard.piraso.api.entry.Entry;

import java.util.Date;
import java.util.EventObject;

/**
 * Defines a read entry event.
 */
public class EntryReadEvent<T> extends EventObject {

    private Entry entry;

    private String id;

    private Date date;

    public EntryReadEvent(Object o, String id, Entry entry, Date date) {
        super(o);

        this.id = id;
        this.entry = entry;
        this.date = date;
    }

    public Entry getEntry() {
        return entry;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }
}
