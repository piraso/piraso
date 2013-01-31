package org.piraso.api.entry;

public enum MessageType {
    TEXT_PLAIN("text/plain"),

    HTML("text/html"),

    XML("text/xml"),

    JSON("application/json");

    private String contentType;

    private MessageType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}
