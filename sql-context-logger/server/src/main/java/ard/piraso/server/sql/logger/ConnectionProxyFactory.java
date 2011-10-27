package ard.piraso.server.sql.logger;

import ard.piraso.api.Level;
import ard.piraso.api.sql.SQLPreferenceEnum;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.logger.MethodCallLoggerListener;
import ard.piraso.server.logger.SimpleMethodLoggerListener;
import ard.piraso.server.proxy.RegexMethodInterceptorAdapter;
import ard.piraso.server.proxy.RegexMethodInterceptorEvent;
import ard.piraso.server.proxy.RegexProxyFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * SQL connection factory, this will create the proxy instance responsible for logging sql connection specific entries.
 */
public class ConnectionProxyFactory extends AbstractSQLProxyFactory<Connection> {

    private static final Level METHOD_CALL_LEVEL = Level.get(SQLPreferenceEnum.CONNECTION_METHOD_CALL_ENABLED.getPropertyName());

    private static final Level BASE_LEVEL = Level.get(SQLPreferenceEnum.CONNECTION_ENABLED.getPropertyName());

    public ConnectionProxyFactory(GroupChainId id) {
        super(id, new RegexProxyFactory<Connection>(Connection.class));

        if(getPref().isConnectionMethodCallEnabled()) {
            factory.addMethodListener(".*", new MethodCallLoggerListener<Connection>(METHOD_CALL_LEVEL, id));
        }

        if(getPref().isConnectionEnabled()) {
            if(!getPref().isConnectionMethodCallEnabled()) {
                factory.addMethodListener("close|commit|rollback", new SimpleMethodLoggerListener<Connection>(BASE_LEVEL, id));
            }

            if(!getPref().isPreparedStatementEnabled()) {
                factory.addMethodListener("prepareStatement", new PreparedStatementListener());
            }
        }
    }

    private class PreparedStatementListener extends RegexMethodInterceptorAdapter<Connection> {
        @Override
        public void afterCall(RegexMethodInterceptorEvent<Connection> evt) {
            PreparedStatement statement = (PreparedStatement) evt.getReturnedValue();
            GroupChainId newId = id.create("statement-", statement.hashCode());

            newId.addProperty(Connection.class, wrappedObject);
            String sql = (String) evt.getInvocation().getArguments()[0];

            evt.setReturnedValue(new PreparedStatementProxyFactory(newId, sql).getProxy(statement));
        }
    }
}
