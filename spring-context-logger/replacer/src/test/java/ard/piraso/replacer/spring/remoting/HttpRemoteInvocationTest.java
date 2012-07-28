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

package ard.piraso.replacer.spring.remoting;

import ard.piraso.api.XStreamUtils;
import junit.framework.TestCase;
import org.springframework.remoting.support.RemoteInvocation;

import java.lang.reflect.Method;

/**
 * Test for {@link HttpRemoteInvocation}
 */
public class HttpRemoteInvocationTest extends TestCase {

    public class TestNestedBean {
        private TestNestedBean test;

        private TestNestedBean self = this;

        private String alvin = "alvin";

        public TestNestedBean(TestNestedBean test) {
            this.test = test;
        }

        public String getAlvin() {
            return alvin;
        }

        public void setTest(TestNestedBean test) {
            this.test = test;
        }

        public TestNestedBean getTest() {
            return test;
        }
    }

    public void testXML() throws Exception {
        RemoteInvocation remote = new RemoteInvocation();

        Method method = String.class.getMethod("valueOf", Object.class);

        remote.setMethodName(method.getName());
        remote.setParameterTypes(method.getParameterTypes());
        remote.setArguments(new Object[] {"Alvin", "de", "Leon", new TestNestedBean(new TestNestedBean(null))});
        HttpRemoteInvocation invocation = new HttpRemoteInvocation(remote);

        System.out.println(XStreamUtils.XSTREAM.toXML(invocation));
    }
}
