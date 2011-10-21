package ard.piraso.api.entry;

/**
 * Contains helper method for processing entries.
 */
public class EntryUtils {

    public static ObjectEntry[] toEntry(Object[] objs) {
        if(objs == null) {
            return null;
        }

        ObjectEntry[] entries = new ObjectEntry[objs.length];

        for(int i = 0; i < objs.length; i++) {
            entries[i] = new ObjectEntry(objs[i]);
        }

        return entries;
    }

    public static StackTraceElementEntry[] toEntry(StackTraceElement[] elements) {
        if(elements == null) {
            return null;
        }

        StackTraceElementEntry[] stackTrace = new StackTraceElementEntry[elements.length];

        for(int i = 0; i < elements.length; i++) {
            stackTrace[i] = new StackTraceElementEntry(elements[i]);
        }

        return stackTrace;
    }
}
