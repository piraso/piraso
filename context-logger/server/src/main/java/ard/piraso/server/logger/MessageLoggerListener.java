package ard.piraso.server.logger;

import ard.piraso.api.Level;
import ard.piraso.api.entry.ElapseTimeEntry;
import ard.piraso.api.entry.MessageEntry;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.dispatcher.ContextLogDispatcher;
import ard.piraso.server.proxy.RegexMethodInterceptorEvent;
import ard.piraso.server.proxy.RegexMethodInterceptorListener;

/**
 * Message logger listener
 */
public class MessageLoggerListener<T> implements RegexMethodInterceptorListener<T> {

    private GroupChainId id;

    private ElapseTimeEntry elapseTime;

    private MessageEntry entry;

    private String message;

    private Level level;

    public MessageLoggerListener(Level level, GroupChainId id, String message) {
        this(level, id, message, null);
    }

    public MessageLoggerListener(Level level, GroupChainId id, String message, ElapseTimeEntry elapseTime) {
        this.id = id;
        this.message = message;
        this.level = level;

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
        entry.getElapseTime().stop();

        ContextLogDispatcher.forward(level, id, entry);
    }

    public void exceptionCall(RegexMethodInterceptorEvent<T> evt) {
        entry.getElapseTime().stop();
        entry.setMessage(evt.getInvocation().getMethod().getName() + ":" + evt.getException().getClass().getName());

        ContextLogDispatcher.forward(level, id, entry);
    }
}
