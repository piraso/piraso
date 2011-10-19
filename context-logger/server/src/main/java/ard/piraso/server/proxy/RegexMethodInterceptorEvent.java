package ard.piraso.server.proxy;

import org.aopalliance.intercept.MethodInvocation;

/**
 * Method interceptor event.
 */
public class RegexMethodInterceptorEvent<T>  {

    private MethodInvocation invocation;

    private T target;

    private RegexMethodInterceptor<T> source;

    private Object returnedValue;

    private Exception exception;
    
    RegexMethodInterceptorEvent(RegexMethodInterceptor<T> source, MethodInvocation invocation) {
        this(source, invocation, null);
    }

    @SuppressWarnings("unchecked")
    RegexMethodInterceptorEvent(RegexMethodInterceptor<T> source, MethodInvocation invocation, Object returnedValue) {
        this.source = source;
        this.invocation = invocation;
        this.target = (T) invocation.getThis();
        this.returnedValue = returnedValue;
    }

    public Exception getException() {
        return exception;
    }

    void setException(Exception exception) {
        this.exception = exception;
    }

    public MethodInvocation getInvocation() {
        return invocation;
    }

    public T getTarget() {
        return target;
    }

    public RegexMethodInterceptor<T> getSource() {
        return source;
    }

    public Object getReturnedValue() {
        return returnedValue;
    }

    public void setReturnedValue(Object newReturnedValue) {
        this.returnedValue = newReturnedValue;
    }
}
