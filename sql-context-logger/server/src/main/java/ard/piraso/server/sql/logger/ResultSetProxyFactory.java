package ard.piraso.server.sql.logger;

import ard.piraso.api.entry.ElapseTimeEntry;
import ard.piraso.api.sql.SQLDataTotalRowsEntry;
import ard.piraso.api.sql.SQLDataViewEntry;
import ard.piraso.api.sql.SQLParameterEntry;
import ard.piraso.server.dispatcher.LogEntryDispatcher;
import ard.piraso.server.logger.MessageLoggerListener;
import ard.piraso.server.logger.MethodCallLoggerListener;
import ard.piraso.server.logger.TraceableID;
import ard.piraso.server.proxy.RegexMethodInterceptorAdapter;
import ard.piraso.server.proxy.RegexMethodInterceptorEvent;
import ard.piraso.server.proxy.RegexProxyFactory;
import ard.piraso.server.sql.IDGenerator;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link ResultSet} proxy logger factory
 */
public class ResultSetProxyFactory extends AbstractSQLProxyFactory<ResultSet> {

    /**
     * Maximum number of record queue size before we start dispatching to monitors.
     * This will ensure that we do not cause {@link OutOfMemoryError}.
     */
    private static final int MAX_RETURN_RESULT = 100;

    private static final IDGenerator GENERATOR = new IDGenerator();

    private ResultSetParameterListener parameterListener;

    /**
     * Stores the record temporarily in memory to be dispatched when
     * the set return size is reached.
     */
    private List<List<SQLParameterEntry>> recordQueue = new ArrayList<List<SQLParameterEntry>>(MAX_RETURN_RESULT);

    private long resultSetId = GENERATOR.next();

    private int totalRowCount = 0;

    public ResultSetProxyFactory(TraceableID id) {
        super(id, new RegexProxyFactory<ResultSet>(ResultSet.class));

        ElapseTimeEntry elapseTime = new ElapseTimeEntry();
        elapseTime.start();

        if(getPref().isResultSetMethodCallEnabled()) {
            factory.addMethodListener(".*", new MethodCallLoggerListener<ResultSet>(id, preference));
        }

        parameterListener = new ResultSetParameterListener();

        factory.addMethodListener("get.*", parameterListener);
        factory.addMethodListener("close", new MessageLoggerListener<ResultSet>(id, "Fetch Elapse Time", elapseTime));
        factory.addMethodListener("next|close", new NextCloseListener());
    }

    private class NextCloseListener extends RegexMethodInterceptorAdapter<ResultSet> {
        @Override
        public void afterCall(RegexMethodInterceptorEvent<ResultSet> evt) {
            // ignore all if max size is already reached
            Method method = evt.getInvocation().getMethod();
            if(method.getName().equals("next") && evt.getReturnedValue() != null &&
                    evt.getReturnedValue().equals(Boolean.TRUE)) {
                totalRowCount++;
            }

            if(CollectionUtils.isNotEmpty(parameterListener.getParameters())) {
                recordQueue.add(new ArrayList<SQLParameterEntry>(parameterListener.getParameters()));
                parameterListener.clear();
            }

            if(!parameterListener.isDisabled() && (recordQueue.size() >= getPref().getMaxDataSize()
                    || recordQueue.size() >= MAX_RETURN_RESULT)) {
                if(totalRowCount >= getPref().getMaxDataSize()) {
                    parameterListener.disable();
                }

                if(CollectionUtils.isNotEmpty(recordQueue)) {
                    LogEntryDispatcher.forward(id, new SQLDataViewEntry(resultSetId, recordQueue));
                    recordQueue.clear();
                }
            }

            if(!parameterListener.isDisabled() && method.getName().equals("close") && CollectionUtils.isNotEmpty(recordQueue)) {
                LogEntryDispatcher.forward(id, new SQLDataViewEntry(resultSetId, recordQueue));
                recordQueue.clear();
            }

            if(method.getName().equals("close")) {
                LogEntryDispatcher.forward(id, new SQLDataTotalRowsEntry(totalRowCount));
            }
        }
    }
}
