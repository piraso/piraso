package ard.piraso.api.entry;

import ard.piraso.api.JacksonUtils;

import java.io.IOException;

/**
 * JSON entry
 */
public class JSONEntry extends Entry implements MessageAwareEntry, ThrowableAwareEntry, StackTraceAwareEntry {

    private ThrowableEntry thrown;

    private String message;

    private String jsonString;

    private StackTraceElementEntry[] stackTrace;

    public JSONEntry() {
    }

    public JSONEntry(String message, Object jsonObj) throws IOException {
        setMessage(message);
        json(jsonObj);
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public void setThrown(ThrowableEntry thrown) {
        this.thrown = thrown;
    }

    public ThrowableEntry getThrown() {
        return thrown;
    }

    public void json(Object obj) throws IOException {
        setJsonString(JacksonUtils.MAPPER.writeValueAsString(obj));
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public StackTraceElementEntry[] getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTraceElementEntry[] stackTrace) {
        this.stackTrace = stackTrace;
    }
}
