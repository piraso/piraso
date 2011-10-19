package ard.piraso.server.sql;

import ard.piraso.api.sql.SQLParameterEntry;
import ard.piraso.server.proxy.RegexMethodInterceptorAdapter;
import ard.piraso.server.proxy.RegexMethodInterceptorEvent;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * ResultSet parameter listener
 */
public class ResultSetParameterListener extends RegexMethodInterceptorAdapter<ResultSet> {
    List<SQLParameterEntry> parameters = new LinkedList<SQLParameterEntry>();

    boolean disabled = false;

    public void clear() {
        parameters.clear();
    }

    public void addParameter(SQLParameterEntry obj) {
        parameters.add(obj);
    }

    public List<SQLParameterEntry> getParameters() {
        return parameters;
    }

    public void disable() {
        disabled = true;
    }

    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void afterCall(RegexMethodInterceptorEvent<ResultSet> evt) {
        if(disabled) return;

        MethodInvocation invocation = evt.getInvocation();
        Method method = invocation.getMethod();
        Object returnedValue = evt.getReturnedValue();

        Class[] types = method.getParameterTypes();
        if(Integer.TYPE.isAssignableFrom(types[0])) {
            Integer index = (Integer) invocation.getArguments()[0];

            addParameter(new SQLParameterEntry(index, method, invocation.getArguments(), returnedValue));
        } else if(String.class.isAssignableFrom(types[0])) {
            String str = (String) invocation.getArguments()[0];

            addParameter(new SQLParameterEntry(str, method, invocation.getArguments(), returnedValue));
        }
    }
}
