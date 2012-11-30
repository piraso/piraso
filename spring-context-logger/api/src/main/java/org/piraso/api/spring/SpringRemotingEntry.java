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

package org.piraso.api.spring;

import org.piraso.api.XStreamUtils;
import org.piraso.api.entry.Entry;
import org.piraso.api.entry.MessageAwareEntry;
import org.piraso.api.entry.XMLAwareEntry;

import java.io.IOException;

/**
 * Spring remoting entry
 */
public class SpringRemotingEntry extends Entry implements MessageAwareEntry, XMLAwareEntry {

    private String message;

    private String xmlString;

    private String url;

    private String serviceInterface;

    private String methodName;

    public SpringRemotingEntry() {
    }

    public SpringRemotingEntry(String message, Object xmlObj) throws IOException {
        setMessage(message);
        toXml(xmlObj);
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(String serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getXmlString() {
        return xmlString;
    }

    public void setXmlString(String xmlString) {
        this.xmlString = xmlString;
    }

    public void toXml(Object obj) throws IOException {
        setXmlString(XStreamUtils.XSTREAM.toXML(obj));
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
