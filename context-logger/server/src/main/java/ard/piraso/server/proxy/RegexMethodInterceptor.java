package ard.piraso.server.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Regular expression method interceptor.
 */
public class RegexMethodInterceptor<T> implements MethodInterceptor {

    private static final Logger LOG = Logger.getLogger(RegexMethodInterceptor.class);

    private Map<String, Set<RegexMethodInterceptorListener<T>>> listeners = new LinkedHashMap<String, Set<RegexMethodInterceptorListener<T>>>();

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Set<RegexMethodInterceptorListener<T>> invokeListeners = getListeners(invocation.getMethod());

        fireBeforeCall(invokeListeners, invocation);
        Object returnValue;

        try {
            returnValue = invocation.proceed();
        } catch(Exception e) {
            fireExceptionCall(invokeListeners, invocation, e);

            throw e;
        }

        return fireAfterCall(invokeListeners, invocation, returnValue);
    }

    private void fireBeforeCall(Set<RegexMethodInterceptorListener<T>> listeners, MethodInvocation invocation) {
        try {
            RegexMethodInterceptorEvent<T> event = new RegexMethodInterceptorEvent<T>(this, invocation);
            for(RegexMethodInterceptorListener<T> listener : listeners) {
                listener.beforeCall(event);
            }
        } catch(Exception e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    private void fireExceptionCall(Set<RegexMethodInterceptorListener<T>> listeners, MethodInvocation invocation, Exception ex) {
        try {
            RegexMethodInterceptorEvent<T> event = new RegexMethodInterceptorEvent<T>(this, invocation);
            event.setException(ex);
            for(RegexMethodInterceptorListener<T> listener : listeners) {
                listener.exceptionCall(event);
            }
        } catch(Exception e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    private Object fireAfterCall(Set<RegexMethodInterceptorListener<T>> listeners, MethodInvocation invocation, Object returnedValue) {
        try {
            RegexMethodInterceptorEvent<T> event = new RegexMethodInterceptorEvent<T>(this, invocation, returnedValue);
            for(RegexMethodInterceptorListener<T> listener : listeners) {
                listener.afterCall(event);
            }

            return event.getReturnedValue();
        } catch(Exception e) {
            LOG.warn(e.getMessage(), e);
        }

        return returnedValue;
    }

    private Set<RegexMethodInterceptorListener<T>> getListeners(Method method) {
        Set<RegexMethodInterceptorListener<T>> matchListeners = new HashSet<RegexMethodInterceptorListener<T>>();

        String methodName = method.getName();
        for(Map.Entry<String, Set<RegexMethodInterceptorListener<T>>> entry : listeners.entrySet()) {
            if(methodName.matches(entry.getKey())) {
                matchListeners.addAll(entry.getValue());
            }
        }

        return matchListeners;
    }

    public void addAllMethodListener(Map<String, Set<RegexMethodInterceptorListener<T>>> newListeners) {
        for(Map.Entry<String, Set<RegexMethodInterceptorListener<T>>> entry : newListeners.entrySet()) {
            for(RegexMethodInterceptorListener<T> newListener : entry.getValue()) {
                addMethodListener(entry.getKey(), newListener);
            }
        }
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
}
