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

package ard.piraso.server;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test for {@link PirasoRequestContext} class.
 */
public class PirasoRequestContextTest {

    private PirasoContext context;

    private PirasoRequestContext pirasoRequestContext;

    @Before
    public void setUp() throws Exception {
        context = mock(PirasoContext.class);
        PirasoContextHolder.setContext(context);

        pirasoRequestContext = new PirasoRequestContext();
    }

    @Test
    public void testNullDelegate() {
        PirasoContextHolder.removeContext();
        pirasoRequestContext = new PirasoRequestContext();

        assertFalse(pirasoRequestContext.isMonitored());
        assertFalse(pirasoRequestContext.isEnabled("some property"));
        assertNull(pirasoRequestContext.getIntValue("some property"));

        pirasoRequestContext.requestOnScope(); // nothing will happen here

        verify(context, times(0)).isMonitored();
        verify(context, times(0)).isEnabled("some property");
        verify(context, times(0)).getIntValue("some property");
        verify(context, times(0)).requestOnScope();
    }

    @Test
    public void testIsMonitored() throws Exception {
        doReturn(true).when(context).isMonitored();

        assertTrue(pirasoRequestContext.isMonitored());
        verify(context, times(1)).isMonitored();
    }

    @Test
    public void testIsEnabled() throws Exception {
        doReturn(true).when(context).isEnabled("property");

        assertTrue(pirasoRequestContext.isEnabled("property"));
        assertFalse(pirasoRequestContext.isEnabled("other property"));

        verify(context, times(2)).isEnabled(anyString());
    }

    @Test
    public void testGetIntValue() throws Exception {
        doReturn(1).when(context).getIntValue("property");

        assertEquals(Integer.valueOf(1), pirasoRequestContext.getIntValue("property"));
        assertEquals(Integer.valueOf(0), pirasoRequestContext.getIntValue("other property"));

        verify(context, times(2)).getIntValue(anyString());
    }

    @Test
    public void testRequestInScope() throws Exception {
        pirasoRequestContext.requestOnScope();

        verify(context, times(1)).requestOnScope();
    }
}
