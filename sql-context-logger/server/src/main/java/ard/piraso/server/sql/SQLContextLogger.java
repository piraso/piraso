package ard.piraso.server.sql;

import ard.piraso.server.logger.TraceableID;
import ard.piraso.server.sql.logger.DataSourceProxyFactory;

import javax.sql.DataSource;

/**
 * Utility class for wrapping {@link DataSource} object with the {@link DataSource} proxy logger.
 */
public class SQLContextLogger {

    /**
     * Wraps the given {@link DataSource} to add support for context logging.
     *
     * @param dataSource {@link DataSource} to add context logging support.
     * @param id the unique identification for this dataSource.
     * @return the proxy {@link DataSource} with context logging support.
     */
    public static DataSource create(DataSource dataSource, String id) {
        DataSourceProxyFactory factory = new DataSourceProxyFactory(new TraceableID(id));

        return factory.getProxy(dataSource);
    }
}
