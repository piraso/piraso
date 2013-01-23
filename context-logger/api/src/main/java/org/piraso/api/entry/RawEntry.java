package org.piraso.api.entry;

public class RawEntry extends Entry {

    private String rawClassName;

    private String rawContent;

    public RawEntry(Long requestId, String rawClassName, String rawContent) {
        this.rawClassName = rawClassName;
        this.rawContent = rawContent;
        this.requestId = requestId;
    }

    public String getRawClassName() {
        return rawClassName;
    }

    public String getRawContent() {
        return rawContent;
    }
}
