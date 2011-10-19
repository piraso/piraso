package ard.piraso.server.sql;

import ard.piraso.api.sql.SQLParameterEntry;
import ard.piraso.server.proxy.RegexMethodInterceptorAdapter;
import ard.piraso.server.proxy.RegexMethodInterceptorEvent;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for collecting statement parameters.
 */
public class StatementParameterListener extends RegexMethodInterceptorAdapter<PreparedStatement> {

    protected static final List<String> VALID_METHOD_NAMES = Arrays.asList(
            "setBoolean",
            "setByte",
            "setShort",
            "setInt",
            "setLong",
            "setFloat",
            "setDouble",
            "setBigDecimal",
            "setString",
            "setBytes",
            "setDate",      // with cal
            "setTime",      // with cal
            "setTimestamp", // with cal
            "setRef",
            "setBlob",
            "setClob",
            "setArray",
            "setNull",      // with type
            "setURL",
            "setAsciiStream", // with length
            "setUnicodeStream",
            "setBinaryStream",
            "setCharacterStream",
            "setObject"        // with type
    );

    private Map<Integer, SQLParameterEntry> parameters = new LinkedHashMap<Integer, SQLParameterEntry>();

    public void clear() {
        parameters.clear();
    }

    public void addParameter(Integer index, SQLParameterEntry obj) {
        parameters.put(index, obj);
    }


    public Map<Integer, SQLParameterEntry> getParameters() {
        return parameters;
    }

    private boolean isValidMethod(String methodName) {
        for(String validName : VALID_METHOD_NAMES) {
            if(validName.equals(methodName)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void beforeCall(RegexMethodInterceptorEvent<PreparedStatement> evt) {
        MethodInvocation invocation = evt.getInvocation();
        Method method = invocation.getMethod();

        if(!isValidMethod(method.getName())) {
            return;
        }

        Class[] types = method.getParameterTypes();
        if(Integer.TYPE.isAssignableFrom(types[0])) {
            Integer index = (Integer) invocation.getArguments()[0];

            addParameter(index, new SQLParameterEntry(index, method, invocation.getArguments()));
        }
    }
}
