package ard.piraso.api.entry;

import java.io.Serializable;

/**
 * Represents a header entry.
 */
public class HeaderEntry<T extends Serializable> extends Entry {
    private String name;

    private T value;

    public HeaderEntry() {
    }

    public HeaderEntry(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
