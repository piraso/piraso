package ard.piraso.api.entry;

/**
 * Defines a message log entry.
 */
public class MessageEntry extends Entry implements ElapseTimeAware {

    private String message;

    private ElapseTimeEntry elapseTime;

    public MessageEntry() {}

    public MessageEntry(String message) {
        this(message, null);
    }

    public MessageEntry(String message, ElapseTimeEntry elapseTime) {
        this.message = message;
        this.elapseTime = elapseTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ElapseTimeEntry getElapseTime() {
        return elapseTime;
    }

    public void setElapseTime(ElapseTimeEntry elapseTime) {
        this.elapseTime = elapseTime;
    }
}
