package ard.piraso.replacer.spring.remoting;

import org.springframework.remoting.support.RemoteInvocation;

import java.util.Arrays;
import java.util.List;

/**
 * Http remote invocation
 */
public class HttpRemoteInvocation {

    private String methodName;

    private List<Object> arguments;

    public HttpRemoteInvocation(RemoteInvocation invocation) {
        this.methodName = invocation.getMethodName();

        if(invocation.getArguments() != null && invocation.getArguments().length > 0) {
            arguments = Arrays.asList(invocation.getArguments());
        }
    }

    public List<Object> getArguments() {
        return arguments;
    }

    public String getMethodName() {
        return methodName;
    }
}
