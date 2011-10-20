package ard.piraso.api.entry;

/**
 * Defines a message log entry.
 */
public class MessageEntry implements Entry, ElapseTimeAware {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageEntry that = (MessageEntry) o;

        if (elapseTime != null ? !elapseTime.equals(that.elapseTime) : that.elapseTime != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + (elapseTime != null ? elapseTime.hashCode() : 0);
        return result;
    }
}
