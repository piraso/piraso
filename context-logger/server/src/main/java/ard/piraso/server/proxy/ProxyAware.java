package ard.piraso.server.proxy;

/**
 * Proxy factory interface.
 */
public interface ProxyAware<T> {

    /**
     * Get the proxy object given the object being proxied.
     *
     * @param wrappedObject the proxied object
     * @return the proxy instance
     */
    public T getProxy(T wrappedObject);
}
