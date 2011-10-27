package ard.piraso.server.sql.logger;

import ard.piraso.api.Level;
import ard.piraso.api.entry.ElapseTimeEntry;
import ard.piraso.api.sql.SQLPreferenceEnum;
import ard.piraso.api.sql.SQLViewEntry;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.dispatcher.ContextLogDispatcher;
import ard.piraso.server.logger.MessageLoggerListener;
import ard.piraso.server.logger.MethodCallLoggerListener;
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

    private static final Level METHOD_CALL_LEVEL = Level.get(SQLPreferenceEnum.PREPARED_STATEMENT_METHOD_CALL_ENABLED.getPropertyName());

    private static final Level BASE_LEVEL = Level.get(SQLPreferenceEnum.PREPARED_STATEMENT_ENABLED.getPropertyName());

    private String sql;

    private StatementParameterListener<PreparedStatement> parameterListener;

    public PreparedStatementProxyFactory(GroupChainId id, String sql) {
        super(id, new RegexProxyFactory<PreparedStatement>(PreparedStatement.class));

        this.sql = sql;

        if(getPref().isPreparedStatementMethodCallEnabled()) {
            factory.addMethodListener(".*", new MethodCallLoggerListener<PreparedStatement>(METHOD_CALL_LEVEL, id));
        }

        if(getPref().isViewSQLEnabled()) {
            parameterListener = new StatementParameterListener<PreparedStatement>();

            factory.addMethodListener("set.*", parameterListener);
        }

        factory.addMethodListener("executeBatch", new MessageLoggerListener<PreparedStatement> (BASE_LEVEL, id, "Execution Elapse Time"));
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

                ContextLogDispatcher.forward(BASE_LEVEL, id, new SQLViewEntry(sql, parameterListener.getParameters(), elapseTime));

                // to be reused
                parameterListener.clear();
            }

            if(getPref().isResultSetEnabled() && ResultSet.class.isAssignableFrom(method.getReturnType())) {
                ResultSet resultSet = (ResultSet) evt.getReturnedValue();
                GroupChainId newId = id.create("resultset-", resultSet.hashCode());

                evt.setReturnedValue(new ResultSetProxyFactory(newId).getProxy(resultSet));
            }
        }
    }
}
