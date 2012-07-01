package ard.piraso.server.java.concurrent;

import ard.piraso.api.Level;
import ard.piraso.api.entry.RequestEntry;
import ard.piraso.api.entry.ResponseEntry;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.PirasoContext;
import ard.piraso.server.PirasoContextHolder;
import ard.piraso.server.dispatcher.ContextLogDispatcher;

/**
 * runnable wrapper
 */
public class PirasoContextRunnableWrapper implements Runnable {

    private Runnable delegate;

    private PirasoContext context;

    private RequestEntry request;

    private ResponseEntry response;

    public PirasoContextRunnableWrapper(Runnable delegate, PirasoContext context, RequestEntry request, ResponseEntry response) {
        this.delegate = delegate;
        this.context = context;
        this.request = request;
        this.response = response;
    }

    public void run() {
        PirasoContextHolder.setContext(context);

        ContextLogDispatcher.forward(Level.SCOPED, new GroupChainId("request"), request);

        try {
            delegate.run();
        } finally {
            ContextLogDispatcher.forward(Level.SCOPED, new GroupChainId("response"), response);
            PirasoContextHolder.removeContext();
        }
    }
}