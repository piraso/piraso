package ard.piraso.server.sql.logger;

import ard.piraso.server.logger.AbstractLoggerProxyFactory;
import ard.piraso.server.logger.TraceableID;
import ard.piraso.server.proxy.RegexProxyFactory;
import ard.piraso.server.sql.SQLPreferenceEvaluator;

/**
 * Base SQL logger factory.
 */
public class AbstractSQLProxyFactory<T> extends AbstractLoggerProxyFactory<T> {

    public AbstractSQLProxyFactory(TraceableID id, RegexProxyFactory<T> factory) {
        super(id, factory);

        preference = new SQLPreferenceEvaluator();
    }

    public SQLPreferenceEvaluator getPref() {
        return (SQLPreferenceEvaluator) preference;
    }
}
