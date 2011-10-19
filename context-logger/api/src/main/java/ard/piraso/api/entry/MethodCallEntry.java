package ard.piraso.api.entry;

import org.apache.commons.lang.ArrayUtils;

import java.lang.reflect.Method;

/**
 * Defines a method call log entry.
 */
public class MethodCallEntry implements Entry, ElapseTimeAware {

    private String methodName;

    private String[] parameterClassNames;

    private ObjectEntry[] arguments;

    private ObjectEntry returnedValue;

    private String returnClassName;

    private ElapseTimeEntry elapseTime;

    private ThrowableEntry thrown;

    private StackTraceElementEntry[] stackTrace;

    public MethodCallEntry() {
    }

    public MethodCallEntry(Method method) {
        this(method, null);
    }

    public MethodCallEntry(Method method, ElapseTimeEntry elapseTime) {
        this(method, elapseTime, null);
    }

    public MethodCallEntry(Method method, ElapseTimeEntry elapseTime, ThrowableEntry thrown) {
        this.elapseTime = elapseTime;
        this.thrown = thrown;
        methodName = method.getName();
        returnClassName = method.getReturnType().getName();

        if(ArrayUtils.isNotEmpty(method.getParameterTypes())) {
            parameterClassNames = new String[method.getParameterTypes().length];
            for(int i = 0; i < method.getParameterTypes().length; i++) {
                parameterClassNames[i] = method.getParameterTypes()[i].getName();
            }
        }
    }

    public ObjectEntry[] getArguments() {
        return arguments;
    }

    public void setArguments(ObjectEntry[] arguments) {
        this.arguments = arguments;
    }

    public ObjectEntry getReturnedValue() {
        return returnedValue;
    }

    public void setReturnedValue(ObjectEntry returnedValue) {
        this.returnedValue = returnedValue;
    }

    public StackTraceElementEntry[] getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTraceElementEntry[] stackTrace) {
        this.stackTrace = stackTrace;
    }

    public ThrowableEntry getThrown() {
        return thrown;
    }

    public void setThrown(ThrowableEntry thrown) {
        this.thrown = thrown;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getParameterClassNames() {
        return parameterClassNames;
    }

    public void setParameterClassNames(String[] parameterClassNames) {
        this.parameterClassNames = parameterClassNames;
    }

    public String getReturnClassName() {
        return returnClassName;
    }

    public void setReturnClassName(String returnClassName) {
        this.returnClassName = returnClassName;
    }

    public ElapseTimeEntry getElapseTime() {
        return elapseTime;
    }

    public void setElapseTime(ElapseTimeEntry elapseTime) {
        this.elapseTime = elapseTime;
    }
}
