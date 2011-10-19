package ard.piraso.server.proxy;

/**
 * Regular expression method interceptor listener.
 */
public interface RegexMethodInterceptorListener<T> {
    /**
     * Will be triggered before a target method is invoked.
     *
     * @param evt the event object
     */
    public void beforeCall(RegexMethodInterceptorEvent<T> evt);

    /**
     * Will be triggered after a target method is invoked.
     *
     * @param evt the event object
     */
    public void afterCall(RegexMethodInterceptorEvent<T> evt);

    /**
     * Will be triggered after a target method is invoked and when an exception is thrown.
     *
     * @param evt the event object
     */
    public void exceptionCall(RegexMethodInterceptorEvent<T> evt);
}
