/*
 * Copyright (c) 2011. Piraso Alvin R. de Leon. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The Piraso licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ard.piraso.server.sql.logger;

import ard.piraso.api.IDGenerator;
import ard.piraso.api.Level;
import ard.piraso.api.entry.ElapseTimeEntry;
import ard.piraso.api.sql.SQLDataTotalRowsEntry;
import ard.piraso.api.sql.SQLDataViewEntry;
import ard.piraso.api.sql.SQLParameterEntry;
import ard.piraso.api.sql.SQLPreferenceEnum;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.dispatcher.ContextLogDispatcher;
import ard.piraso.server.logger.MessageLoggerListener;
import ard.piraso.server.logger.MethodCallLoggerListener;
import ard.piraso.server.proxy.RegexMethodInterceptorAdapter;
import ard.piraso.server.proxy.RegexMethodInterceptorEvent;
import ard.piraso.server.proxy.RegexProxyFactory;
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

    private static final Level METHOD_CALL_LEVEL = Level.get(SQLPreferenceEnum.CONNECTION_METHOD_CALL_ENABLED.getPropertyName());

    private static final Level BASE_LEVEL = Level.get(SQLPreferenceEnum.CONNECTION_ENABLED.getPropertyName());

    private ResultSetParameterListener parameterCollector;

    /**
     * Stores the record temporarily in memory to be dispatched when
     * the set return size is reached.
     */
    private List<List<SQLParameterEntry>> recordQueue = new ArrayList<List<SQLParameterEntry>>(MAX_RETURN_RESULT);

    private long resultSetId = GENERATOR.next();

    private int totalRowCount = 0;

    public ResultSetProxyFactory(GroupChainId id) {
        super(id, new RegexProxyFactory<ResultSet>(ResultSet.class));

        ElapseTimeEntry elapseTime = new ElapseTimeEntry();
        elapseTime.start();

        if(getPref().isResultSetMethodCallEnabled()) {
            factory.addMethodListener(".*", new MethodCallLoggerListener<ResultSet>(METHOD_CALL_LEVEL, id));
        }

        parameterCollector = new ResultSetParameterListener();

        factory.addMethodListener("get.*", parameterCollector);
        factory.addMethodListener("close", new MessageLoggerListener<ResultSet>(BASE_LEVEL, id, "Fetch Elapse Time", elapseTime));
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

            if(CollectionUtils.isNotEmpty(parameterCollector.getParameters())) {
                recordQueue.add(new ArrayList<SQLParameterEntry>(parameterCollector.getParameters()));
                parameterCollector.clear();
            }

            if(!parameterCollector.isDisabled() && (recordQueue.size() >= getPref().getMaxDataSize()
                    || recordQueue.size() >= MAX_RETURN_RESULT)) {
                if(totalRowCount >= getPref().getMaxDataSize()) {
                    parameterCollector.disable();
                }

                if(CollectionUtils.isNotEmpty(recordQueue)) {
                    ContextLogDispatcher.forward(BASE_LEVEL, id, new SQLDataViewEntry(resultSetId, recordQueue));
                    recordQueue.clear();
                }
            }

            if(!parameterCollector.isDisabled() && method.getName().equals("close") && CollectionUtils.isNotEmpty(recordQueue)) {
                ContextLogDispatcher.forward(BASE_LEVEL, id, new SQLDataViewEntry(resultSetId, recordQueue));
                recordQueue.clear();
            }

            if(method.getName().equals("close")) {
                ContextLogDispatcher.forward(BASE_LEVEL, id, new SQLDataTotalRowsEntry(totalRowCount));
            }
        }
    }
}
