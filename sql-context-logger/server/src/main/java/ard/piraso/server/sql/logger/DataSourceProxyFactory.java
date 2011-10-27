package ard.piraso.server.sql.logger;

import ard.piraso.server.GroupChainId;
import ard.piraso.server.proxy.RegexMethodInterceptorAdapter;
import ard.piraso.server.proxy.RegexMethodInterceptorEvent;
import ard.piraso.server.proxy.RegexProxyFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * {@link DataSource} logger proxy factory.
 */
public class DataSourceProxyFactory extends AbstractSQLProxyFactory<DataSource> {

    public DataSourceProxyFactory(GroupChainId id) {
        super(id, new RegexProxyFactory<DataSource>(DataSource.class));

        factory.addMethodListener("getConnection", new GetConnectionListener());
    }

    private class GetConnectionListener extends RegexMethodInterceptorAdapter<DataSource> {
        @Override
        public void afterCall(RegexMethodInterceptorEvent<DataSource> evt) {
            // the current request retrieves a db connection
            // which means the current request is in logging scope.
            evaluator.requestOnScope();

            if(getPref().isConnectionEnabled()) {
                Connection connection = (Connection) evt.getReturnedValue();
                GroupChainId newId = id.create("connection-", connection.hashCode());

                evt.setReturnedValue(new ConnectionProxyFactory(newId).getProxy(connection));
            }
        }
    }
}
