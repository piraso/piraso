package ard.piraso.api.entry;

import org.apache.commons.lang.ArrayUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodCallEntry that = (MethodCallEntry) o;

        if (!Arrays.equals(arguments, that.arguments)) return false;
        if (elapseTime != null ? !elapseTime.equals(that.elapseTime) : that.elapseTime != null) return false;
        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;
        if (!Arrays.equals(parameterClassNames, that.parameterClassNames)) return false;
        if (returnClassName != null ? !returnClassName.equals(that.returnClassName) : that.returnClassName != null)
            return false;
        if (returnedValue != null ? !returnedValue.equals(that.returnedValue) : that.returnedValue != null)
            return false;
        if (!Arrays.equals(stackTrace, that.stackTrace)) return false;
        if (thrown != null ? !thrown.equals(that.thrown) : that.thrown != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = methodName != null ? methodName.hashCode() : 0;
        result = 31 * result + (parameterClassNames != null ? Arrays.hashCode(parameterClassNames) : 0);
        result = 31 * result + (arguments != null ? Arrays.hashCode(arguments) : 0);
        result = 31 * result + (returnedValue != null ? returnedValue.hashCode() : 0);
        result = 31 * result + (returnClassName != null ? returnClassName.hashCode() : 0);
        result = 31 * result + (elapseTime != null ? elapseTime.hashCode() : 0);
        result = 31 * result + (thrown != null ? thrown.hashCode() : 0);
        result = 31 * result + (stackTrace != null ? Arrays.hashCode(stackTrace) : 0);
        return result;
    }
}
