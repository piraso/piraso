package ard.piraso.server;

/**
 * Http request context preference.
 */
public class PirasoRequestContext implements ContextPreference {

    /**
     * The delegate instance
     */
    private PirasoContext delegate;

    /**
     * Construct this request context
     */
    public PirasoRequestContext() {
        this.delegate = PirasoContextHolder.getContext();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isMonitored() {
        if(delegate == null) {
            return false;
        }

        return delegate.isMonitored();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEnabled(String property) {
        if(delegate == null) {
            return false;
        }

        return delegate.isEnabled(property);
    }

    /**
     * {@inheritDoc}
     */
    public Integer getIntValue(String property) {
        if(delegate == null) {
            return null;
        }

        return delegate.getIntValue(property);
    }

    /**
     *  {@inheritDoc}
     */
    public void requestOnScope() {
        if(delegate != null) {
            delegate.requestOnScope();
        }
    }
}
