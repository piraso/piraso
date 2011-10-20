package ard.piraso.api.entry;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * An entry with elapse time.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElapseTimeEntry implements Entry {

    private long startTime;

    private long endTime;

    public ElapseTimeEntry() {
        this(0, 0);
    }

    public ElapseTimeEntry(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void restart() {
        this.startTime = System.currentTimeMillis();
    }

    public void start() {
        if(startTime > 0) {
            return;
        }

        this.startTime = System.currentTimeMillis();
    }

    public void stop() {
        if(startTime <= 0) {
            startTime = System.currentTimeMillis();
        }

        this.endTime = System.currentTimeMillis();
    }

    public long getElapseTime() {
        return endTime - startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElapseTimeEntry that = (ElapseTimeEntry) o;

        if (endTime != that.endTime) return false;
        if (startTime != that.startTime) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (startTime ^ (startTime >>> 32));
        result = 31 * result + (int) (endTime ^ (endTime >>> 32));
        return result;
    }
}
