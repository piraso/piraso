package ard.piraso.api.entry;

import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;

/**
 * Defines an exception entry.
 */
public class ThrowableEntry implements Entry {
    private String message;

    private ThrowableEntry cause;

    private StackTraceElementEntry[] stackTrace;

    public ThrowableEntry() {}

    public ThrowableEntry(Throwable e) {
        message = e.getMessage();

        StackTraceElement[] elements = e.getStackTrace();
        if(ArrayUtils.isNotEmpty(elements)) {
            stackTrace = new StackTraceElementEntry[elements.length];

            for(int i = 0; i < elements.length; i++) {
                stackTrace[i] = new StackTraceElementEntry(elements[i]);
            }
        }

        if(e.getCause() != null) {
            cause = new ThrowableEntry(e.getCause());
        }
    }

    public StackTraceElementEntry[] getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTraceElementEntry[] stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ThrowableEntry getCause() {
        return cause;
    }

    public void setCause(ThrowableEntry cause) {
        this.cause = cause;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThrowableEntry that = (ThrowableEntry) o;

        if (cause != null ? !cause.equals(that.cause) : that.cause != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (!Arrays.equals(stackTrace, that.stackTrace)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + (cause != null ? cause.hashCode() : 0);
        result = 31 * result + (stackTrace != null ? Arrays.hashCode(stackTrace) : 0);
        return result;
    }
}
