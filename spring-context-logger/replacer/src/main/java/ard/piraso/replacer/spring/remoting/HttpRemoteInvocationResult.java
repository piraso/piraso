package ard.piraso.replacer.spring.remoting;

import org.springframework.remoting.support.RemoteInvocationResult;

/**
 * http remote invocation result
 */
public class HttpRemoteInvocationResult {

    private String className;

    private Object resultValue;

    public HttpRemoteInvocationResult(RemoteInvocationResult result) {
        this.resultValue = result.getValue();

        if(resultValue != null) {
            className = resultValue.getClass().getName();
        }
    }

    public String getClassName() {
        return className;
    }

    public Object getResultValue() {
        return resultValue;
    }
}
