package ard.piraso.server.logger;

import ard.piraso.api.entry.ElapseTimeEntry;
import ard.piraso.api.entry.MessageEntry;
import ard.piraso.server.dispatcher.ContextLogDispatcher;
import ard.piraso.server.proxy.RegexMethodInterceptorEvent;
import ard.piraso.server.proxy.RegexMethodInterceptorListener;

/**
 * Message logger listener
 */
public class MessageLoggerListener<T> implements RegexMethodInterceptorListener<T> {

    private TraceableID id;

    private ElapseTimeEntry elapseTime;

    private MessageEntry entry;

    private String message;

    private String preferenceProperty;

    public MessageLoggerListener(String preferenceProperty, TraceableID id, String message) {
        this(preferenceProperty, id, message, null);
    }

    public MessageLoggerListener(String preferenceProperty, TraceableID id, String message, ElapseTimeEntry elapseTime) {
        this.id = id;
        this.message = message;
        this.preferenceProperty = preferenceProperty;

        if(elapseTime == null) {
            this.elapseTime = new ElapseTimeEntry();
        } else {
            this.elapseTime = elapseTime;
        }
    }

    public void beforeCall(RegexMethodInterceptorEvent<T> evt) {
        entry = new MessageEntry(message, elapseTime);
        entry.getElapseTime().start();
    }

    public void afterCall(RegexMethodInterceptorEvent<T> evt) {
        assert entry != null;

        entry.getElapseTime().stop();
        ContextLogDispatcher.forward(id, entry);
    }

    public void exceptionCall(RegexMethodInterceptorEvent<T> evt) {
        assert entry != null;

        entry.getElapseTime().stop();
        entry.setMessage(evt.getInvocation().getMethod().getName() + ":" + evt.getException().getClass().getName());

        ContextLogDispatcher.forward(preferenceProperty, id, entry);
    }
}
