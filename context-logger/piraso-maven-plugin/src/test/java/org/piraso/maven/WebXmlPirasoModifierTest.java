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

package org.piraso.maven;

import org.junit.Test;

import java.io.File;

/**
 * Test for {@link WebXmlPirasoModifier} class.
 */
public class WebXmlPirasoModifierTest {
    @Test
    public void testExecute() throws Exception {
        WebXmlPirasoModifier modifier = new WebXmlPirasoModifier();

        File webXml = new File(WebXmlPirasoModifier.class.getResource("/web.xml").getFile());
        File parent = new File(webXml.getParentFile(), "new");

        if(!parent.isDirectory()) {
            parent.mkdirs();
        }

        modifier.setPirasoLoggingPath("/piraso/logging");
        modifier.setWebXml(webXml);
        modifier.setOutputDirectory(parent);

        modifier.execute();
    }

    @Test
    public void testExecute2() throws Exception {
        WebXmlPirasoModifier modifier = new WebXmlPirasoModifier();

        File webXml = new File(WebXmlPirasoModifier.class.getResource("/web2.xml").getFile());
        File parent = new File(webXml.getParentFile(), "new2");

        if(!parent.isDirectory()) {
            parent.mkdirs();
        }

        modifier.setPirasoLoggingPath("/piraso/logging");
        modifier.setWebXml(webXml);
        modifier.setOutputDirectory(parent);

        modifier.execute();
    }
}
