package ard.piraso.server.logger;

import ard.piraso.server.GroupChainId;
import ard.piraso.server.proxy.ProxyAware;
import ard.piraso.server.proxy.RegexProxyFactory;

/**
 * Base class for logger factory.
 */
public abstract class AbstractLoggerProxyFactory<T> implements ProxyAware<T> {

    protected RegexProxyFactory<T> factory;

    protected T wrappedObject;

    protected GroupChainId id;

    public AbstractLoggerProxyFactory(GroupChainId id, RegexProxyFactory<T> factory) {
        this.factory = factory;
        this.id = id;
    }

    public T getProxy(T wrappedObject) {
        this.wrappedObject = wrappedObject;
        return factory.getProxy(wrappedObject);
    }
}
