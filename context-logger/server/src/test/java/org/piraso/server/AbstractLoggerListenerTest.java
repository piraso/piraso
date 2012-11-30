/*
 * Copyright (c) 2012. Piraso Alvin R. de Leon. All Rights Reserved.
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

package org.piraso.server;

import org.piraso.api.Level;
import org.piraso.api.entry.Entry;
import org.piraso.server.dispatcher.ContextLogDispatcher;
import org.junit.Before;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * Base class for logger listener
 */
public abstract class AbstractLoggerListenerTest {

    protected PirasoContext context;

    protected Entry caughtEntry;

    protected GroupChainId id;

    @Before
    public void setUp() throws Exception {
        id = new GroupChainId("test");
        context = mock(PirasoContext.class);

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                caughtEntry = (Entry) invocationOnMock.getArguments()[2];
                return invocationOnMock.callRealMethod();
            }
        }).when(context).log(Matchers.<Level>any(), any(GroupChainId.class), any(Entry.class));

        PirasoContextHolder.setContext(context);
        ContextLogDispatcher.clearListeners();
    }
}
