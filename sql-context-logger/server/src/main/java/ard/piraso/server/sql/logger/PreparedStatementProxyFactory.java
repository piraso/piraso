package ard.piraso.server.sql.logger;

import ard.piraso.api.entry.ElapseTimeEntry;
import ard.piraso.api.sql.SQLViewEntry;
import ard.piraso.server.LogEntryDispatcher;
import ard.piraso.server.logger.MessageLoggerListener;
import ard.piraso.server.logger.MethodCallLoggerListener;
import ard.piraso.server.logger.TraceableID;
import ard.piraso.server.proxy.RegexMethodInterceptorAdapter;
import ard.piraso.server.proxy.RegexMethodInterceptorEvent;
import ard.piraso.server.proxy.RegexProxyFactory;
import org.apache.commons.lang.ArrayUtils;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * {@link PreparedStatement} proxy logger factory.
 */
public class PreparedStatementProxyFactory extends AbstractSQLProxyFactory<PreparedStatement> {

    private String sql;

    private StatementParameterListener<PreparedStatement> parameterListener;

    public PreparedStatementProxyFactory(TraceableID id, String sql) {
        super(id, new RegexProxyFactory<PreparedStatement>(PreparedStatement.class));

        this.sql = sql;

        if(getPref().isPreparedStatementMethodCallEnabled()) {
            factory.addMethodListener(".*", new MethodCallLoggerListener<PreparedStatement>(id, preference));
        }

        if(getPref().isViewSQLEnabled()) {
            parameterListener = new StatementParameterListener<PreparedStatement>();

            factory.addMethodListener("set.*", parameterListener);
        }

        factory.addMethodListener("executeBatch", new MessageLoggerListener<PreparedStatement> (id, "Execution Elapse Time"));
        factory.addMethodListener("executeQuery|executeUpdate|execute|addBatch", new ExecuteSQLListener());
    }

    private class ExecuteSQLListener extends RegexMethodInterceptorAdapter<PreparedStatement> {
        private ElapseTimeEntry elapseTime = new ElapseTimeEntry();

        @Override
        public void beforeCall(RegexMethodInterceptorEvent<PreparedStatement> evt) {
            elapseTime.start();
        }

        @Override
        public void afterCall(RegexMethodInterceptorEvent<PreparedStatement> evt) {
            Method method = evt.getInvocation().getMethod();

            // ensure to only listen to method with no arguments
            if(!ArrayUtils.isEmpty(method.getParameterTypes())) {
                return;
            }

            if(getPref().isViewSQLEnabled()) {
                elapseTime.stop();

                LogEntryDispatcher.forward(id, new SQLViewEntry(sql, parameterListener.getParameters(), elapseTime));

                // to be reused
                parameterListener.clear();
            }

            if(getPref().isResultSetEnabled() && ResultSet.class.isAssignableFrom(method.getReturnType())) {
                ResultSet resultSet = (ResultSet) evt.getReturnedValue();
                TraceableID newId = id.create("resultset-", resultSet.hashCode());

                evt.setReturnedValue(new ResultSetProxyFactory(newId).getProxy(resultSet));
            }
        }
    }
}
