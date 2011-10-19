package ard.piraso.server.proxy;

/**
 * Proxy interceptor aware.
 */
public class ProxyInterceptorAware<T> {
    private RegexMethodInterceptor<T> interceptor;

    private T proxy;

    public ProxyInterceptorAware(T proxy, RegexMethodInterceptor<T> interceptor) {
        this.proxy = proxy;
        this.interceptor = interceptor;
    }

    public RegexMethodInterceptor<T> getInterceptor() {
        return interceptor;
    }

    public T getProxy() {
        return proxy;
    }
}
