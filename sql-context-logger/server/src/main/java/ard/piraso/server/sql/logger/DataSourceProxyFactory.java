package ard.piraso.server.sql.logger;

import ard.piraso.server.logger.TraceableID;
import ard.piraso.server.proxy.RegexMethodInterceptorAdapter;
import ard.piraso.server.proxy.RegexMethodInterceptorEvent;
import ard.piraso.server.proxy.RegexProxyFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * {@link DataSource} logger proxy factory.
 */
public class DataSourceProxyFactory extends AbstractSQLProxyFactory<DataSource> {

    public DataSourceProxyFactory(TraceableID id) {
        super(id, new RegexProxyFactory<DataSource>(DataSource.class));

        factory.addMethodListener("getConnection", new GetConnectionListener());
    }

    private class GetConnectionListener extends RegexMethodInterceptorAdapter<DataSource> {
        @Override
        public void afterCall(RegexMethodInterceptorEvent<DataSource> evt) {
            if(getPref().isConnectionEnabled()) {
                Connection connection = (Connection) evt.getReturnedValue();
                TraceableID newId = id.create("connection-", connection.hashCode());

                evt.setReturnedValue(new ConnectionProxyFactory(newId).getProxy(connection));
            }
        }
    }
}
