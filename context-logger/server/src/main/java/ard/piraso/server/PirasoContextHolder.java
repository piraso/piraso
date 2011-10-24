package ard.piraso.server;

import org.apache.commons.lang.Validate;

/**
 * This will contain the {@link PirasoContext} instance for the current thread.
 */
public class PirasoContextHolder {

    /**
     * Thread context holder.
     */
    private static final ThreadLocal<PirasoContext> pirasoContextHolder = new ThreadLocal<PirasoContext>();

    /**
     * Sets the current {@link PirasoContext} thread instance.
     *
     * @param context the set thread instance
     */
    public static void setContext(PirasoContext context) {
        Validate.notNull(context, "'context' should not be null.");

        pirasoContextHolder.set(context);
    }

    /**
     * Remove the current {@link PirasoContext} thread instance.
     */
    public static void removeContext() {
        pirasoContextHolder.remove();
    }

    /**
     * Retrieves the current  {@link PirasoContext} thread instance.
     *
     * @return the current  {@link PirasoContext} thread instance.
     */
    public static PirasoContext getContext() {
        return pirasoContextHolder.get();
    }

    /**
     * Don't let anybody instantiate this class.
     */
    private PirasoContextHolder() {}
}
