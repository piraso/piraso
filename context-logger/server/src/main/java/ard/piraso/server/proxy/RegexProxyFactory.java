package ard.piraso.server.proxy;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Regular expression proxy factory.
 */
public class RegexProxyFactory<T> implements ProxyAware<T> {

    private Class<T> clazz;

    private Map<String, Set<RegexMethodInterceptorListener<T>>> listeners = new HashMap<String, Set<RegexMethodInterceptorListener<T>>>();

    public RegexProxyFactory(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T getProxy(T object) {
        ProxyInterceptorAware<T> proxy = getProxyInterceptor(object);

        return proxy.getProxy();
    }

    @SuppressWarnings("unchecked")
    public ProxyInterceptorAware<T> getProxyInterceptor(T object) {
        RegexMethodInterceptor<T> wrapper = new RegexMethodInterceptorWrapper(new RegexMethodInterceptor<T>(), object);
        wrapper.addAllMethodListener(listeners);

        return new ProxyInterceptorAware<T>((T) ProxyFactory.getProxy(clazz, wrapper), wrapper);
    }

    public void addMethodListener(String regex, RegexMethodInterceptorListener<T> listener) {
        if(listener == null) {
            throw new IllegalArgumentException("listener cannot be null.");
        }

        Set<RegexMethodInterceptorListener<T>> set = listeners.get(regex);
        if(set == null) {
            set = new HashSet<RegexMethodInterceptorListener<T>>();
            listeners.put(regex, set);
        }
        
        set.add(listener);
    }
    
    private class RegexMethodInterceptorWrapper extends RegexMethodInterceptor<T> {

        private RegexMethodInterceptor<T> wrappedObject;

        private T target;

        private RegexMethodInterceptorWrapper(RegexMethodInterceptor<T> wrappedObject, T target) {
            this.wrappedObject = wrappedObject;
            this.target = target;
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            return wrappedObject.invoke(new MethodInvocationWrapper(invocation, target));
        }

        @Override
        public void addAllMethodListener(Map<String, Set<RegexMethodInterceptorListener<T>>> newListeners) {
            wrappedObject.addAllMethodListener(newListeners);
        }

        @Override
        public void addMethodListener(String regex, RegexMethodInterceptorListener<T> tRegexMethodInterceptorListener) {
            wrappedObject.addMethodListener(regex, tRegexMethodInterceptorListener);
        }
    }

    private class MethodInvocationWrapper implements MethodInvocation {
        private MethodInvocation wrappedObject;

        private T target;

        private MethodInvocationWrapper(MethodInvocation wrappedObject, T target) {
            this.wrappedObject = wrappedObject;
            this.target = target;
        }

        public Method getMethod() {
            return wrappedObject.getMethod();
        }

        public Object[] getArguments() {
            return wrappedObject.getArguments();
        }

        public Object proceed() throws Throwable {
            Method method = getMethod();

            return method.invoke(target, getArguments());
        }

        public Object getThis() {
            return target;
        }

        public AccessibleObject getStaticPart() {
            return wrappedObject.getStaticPart();
        }
    }
}
