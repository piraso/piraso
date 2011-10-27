package ard.piraso.server.logger;

import ard.piraso.api.Level;
import ard.piraso.api.entry.*;
import ard.piraso.server.GeneralPreferenceEvaluator;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.dispatcher.ContextLogDispatcher;
import ard.piraso.server.proxy.RegexMethodInterceptorEvent;
import ard.piraso.server.proxy.RegexMethodInterceptorListener;

/**
 * Method call logger listener.
 */
public class MethodCallLoggerListener<T> implements RegexMethodInterceptorListener<T> {

    private ElapseTimeEntry elapseTime = new ElapseTimeEntry();

    private MethodCallEntry entry;

    private GroupChainId id;

    private GeneralPreferenceEvaluator preference;

    private Level level;

    public MethodCallLoggerListener(Level level, GroupChainId id) {
        this.id = id;
        this.preference = new GeneralPreferenceEvaluator();
        this.level = level;
    }

    public void beforeCall(RegexMethodInterceptorEvent<T> evt) {
        entry = new MethodCallEntry(evt.getInvocation().getMethod(), elapseTime);

        Object[] arguments = evt.getInvocation().getArguments();
        entry.setArguments(EntryUtils.toEntry(arguments));

        // method stack trace only if debug is enabled
        if(preference.isStackTraceEnabled()) {
            entry.setStackTrace(EntryUtils.toEntry(Thread.currentThread().getStackTrace()));
        }

        elapseTime.start();
    }

    public void afterCall(RegexMethodInterceptorEvent<T> evt) {
        elapseTime.stop();
        entry.setReturnedValue(new ObjectEntry(evt.getReturnedValue()));

        ContextLogDispatcher.forward(level, id, entry);
    }

    public void exceptionCall(RegexMethodInterceptorEvent<T> evt) {
        entry.setThrown(new ThrowableEntry(evt.getException()));
        elapseTime.stop();

        ContextLogDispatcher.forward(level, id, entry);
    }
}
