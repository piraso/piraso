package ard.piraso.server.sql.logger;

import ard.piraso.server.GroupChainId;
import ard.piraso.server.logger.AbstractLoggerProxyFactory;
import ard.piraso.server.proxy.RegexProxyFactory;
import ard.piraso.server.sql.SQLPreferenceEvaluator;

/**
 * Base SQL logger factory.
 */
public class AbstractSQLProxyFactory<T> extends AbstractLoggerProxyFactory<T> {

    SQLPreferenceEvaluator evaluator = new SQLPreferenceEvaluator();

    public AbstractSQLProxyFactory(GroupChainId id, RegexProxyFactory<T> factory) {
        super(id, factory);
    }

    public SQLPreferenceEvaluator getPref() {
        return evaluator;
    }
}
