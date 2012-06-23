package ard.piraso.server.java.concurrent;

import ard.piraso.server.PirasoContext;
import ard.piraso.server.PirasoContextHolder;

/**
 * runnable wrapper
 */
public class PirasoContextRunnableWrapper implements Runnable {

    private Runnable delegate;

    private PirasoContext context;

    public PirasoContextRunnableWrapper(Runnable delegate, PirasoContext context) {
        this.delegate = delegate;
        this.context = context;
    }

    public void run() {
        PirasoContextHolder.setContext(context);

        try {
            delegate.run();
        } finally {
            PirasoContextHolder.removeContext();
        }
    }
}