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

package ard.piraso.server.log4j;

import ard.piraso.api.Level;
import ard.piraso.api.entry.Entry;
import ard.piraso.api.log4j.Log4jEntry;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.PirasoContext;
import ard.piraso.server.PirasoContextHolder;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Test for {@link Log4jContextLogger} class.
 */
public class Log4jContextLoggerTest {

    protected PirasoContext context;

    protected Entry caughtEntry;

    @Before
    public void setUp() throws Exception {
        context = mock(PirasoContext.class);

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                caughtEntry = (Entry) invocationOnMock.getArguments()[2];
                return invocationOnMock.callRealMethod();
            }
        }).when(context).log(Matchers.<Level>any(), any(GroupChainId.class), any(Entry.class));
    }

    @Test
    public void testInit() throws Exception {
        new Log4jContextLogger().init();

        when(context.isRegexEnabled(Matchers.<String>any())).thenReturn(true);

        Logger log = Logger.getLogger("test");

        // ensure that this is within context
        PirasoContextHolder.setContext(context);
        log.info("hello");
        PirasoContextHolder.removeContext();

        assertTrue("Not log4j entry.", Log4jEntry.class.isInstance(caughtEntry));
        assertEquals("hello", ((Log4jEntry) caughtEntry).getMessage());
        assertEquals("INFO", ((Log4jEntry) caughtEntry).getLogLevel());
    }

    @Test
    public void testNotLogging() throws Exception {
        new Log4jContextLogger().init();

        when(context.isRegexEnabled(Matchers.<String>any())).thenReturn(false);

        Logger log = Logger.getLogger("test");

        // ensure that this is within context
        PirasoContextHolder.setContext(context);
        log.info("hello");
        PirasoContextHolder.removeContext();

        assertNull(caughtEntry);
    }
}
