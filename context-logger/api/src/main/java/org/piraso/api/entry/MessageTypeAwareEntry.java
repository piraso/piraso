package org.piraso.api.entry;

public interface MessageTypeAwareEntry extends MessageAwareEntry {

    /**
     * Determines the entry content type.
     *
     * @return the entry content type
     */
    String getMessageType();
}
