package ard.piraso.api.entry;

/**
 * An entry with elapse time.
 */
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
}
