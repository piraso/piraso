package ard.piraso.server.proxy;

/**
 * Regular expression method interceptor listener adapter.
 */
public class RegexMethodInterceptorAdapter<T> implements RegexMethodInterceptorListener<T> {

    /**
     * {@inheritDoc}
     */
    public void beforeCall(RegexMethodInterceptorEvent<T> evt) {}

    /**
     * {@inheritDoc}
     */
    public void afterCall(RegexMethodInterceptorEvent<T> evt) {}

    /**
     * {@inheritDoc}
     */
    public void exceptionCall(RegexMethodInterceptorEvent<T> evt) {}
}
