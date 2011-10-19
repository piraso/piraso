package ard.piraso.server.logger;

import ard.piraso.api.entry.*;
import ard.piraso.server.GeneralPreferenceEvaluator;
import ard.piraso.server.LogEntryDispatcher;
import ard.piraso.server.proxy.RegexMethodInterceptorEvent;
import ard.piraso.server.proxy.RegexMethodInterceptorListener;
import org.apache.commons.lang.ArrayUtils;

/**
 * Method call logger listener.
 */
public class MethodCallLoggerListener<T> implements RegexMethodInterceptorListener<T> {

    private ElapseTimeEntry elapseTime = new ElapseTimeEntry();

    private MethodCallEntry entry;

    private TraceableID id;

    private GeneralPreferenceEvaluator preference;

    public MethodCallLoggerListener(TraceableID id, GeneralPreferenceEvaluator preference) {
        this.id = id;
        this.preference = preference;
    }

    public void beforeCall(RegexMethodInterceptorEvent<T> evt) {
        entry = new MethodCallEntry(evt.getInvocation().getMethod(), elapseTime);

        Object[] arguments = evt.getInvocation().getArguments();

        if(ArrayUtils.isNotEmpty(arguments)) {
            ObjectEntry[] argumentEntries = new ObjectEntry[arguments.length];

            for(int i = 0; i < arguments.length; i++) {
                argumentEntries[i] = new ObjectEntry(arguments[i]);
            }

            entry.setArguments(argumentEntries);
        }

        // method stack trace only if debug is enabled
        if(preference.isStackTraceEnabled()) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            StackTraceElementEntry[] stackTrace = new StackTraceElementEntry[elements.length];

            for(int i = 0; i < elements.length; i++) {
                stackTrace[i] = new StackTraceElementEntry(elements[i]);
            }

            entry.setStackTrace(stackTrace);
        }

        elapseTime.start();
    }

    public void afterCall(RegexMethodInterceptorEvent<T> evt) {
        assert entry != null;

        elapseTime.stop();
        entry.setReturnedValue(new ObjectEntry(evt.getReturnedValue()));

        LogEntryDispatcher.forward(id, entry);
    }

    public void exceptionCall(RegexMethodInterceptorEvent<T> evt) {
        assert entry != null;

        entry.setThrown(new ThrowableEntry(evt.getException()));
        elapseTime.stop();

        LogEntryDispatcher.forward(id, entry);
    }
}
