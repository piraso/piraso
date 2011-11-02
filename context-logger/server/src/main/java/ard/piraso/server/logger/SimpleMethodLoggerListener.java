package ard.piraso.server.logger;

import ard.piraso.api.Level;
import ard.piraso.api.entry.ElapseTimeEntry;
import ard.piraso.api.entry.MessageEntry;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.dispatcher.ContextLogDispatcher;
import ard.piraso.server.proxy.RegexMethodInterceptorEvent;
import ard.piraso.server.proxy.RegexMethodInterceptorListener;

/**
 * simple method call logger listener.
 */
public class SimpleMethodLoggerListener<T> implements RegexMethodInterceptorListener<T> {

    private GroupChainId id;

    private ElapseTimeEntry elapseTime;

    private MessageEntry entry;

    private Level level;

    public SimpleMethodLoggerListener(Level level, GroupChainId id) {
        this(level, id, null);
    }

    public SimpleMethodLoggerListener(Level level, GroupChainId id, ElapseTimeEntry elapseTime) {
        this.id = id;
        this.level = level;

        if(elapseTime == null) {
            this.elapseTime = new ElapseTimeEntry();
        } else {
            this.elapseTime = elapseTime;
        }
    }

    public void beforeCall(RegexMethodInterceptorEvent<T> evt) {
        entry = new MessageEntry(evt.getInvocation().getMethod().getName(), elapseTime);
        entry.getElapseTime().start();
    }

    public void afterCall(RegexMethodInterceptorEvent<T> evt) {
        entry.getElapseTime().stop();

        ContextLogDispatcher.forward(level, id, entry);
    }

    public void exceptionCall(RegexMethodInterceptorEvent<T> evt) {
        entry.getElapseTime().stop();
        entry.setMessage(evt.getInvocation().getMethod().getName() + ": " + evt.getException().getClass().getName());

        ContextLogDispatcher.forward(level, id, entry);
    }
}
