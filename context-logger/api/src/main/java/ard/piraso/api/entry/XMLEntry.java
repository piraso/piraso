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

package ard.piraso.api.entry;

import ard.piraso.api.XStreamUtils;

import java.io.IOException;

/**
 * XML entry
 */
public class XMLEntry extends Entry implements MessageAwareEntry, ThrowableAwareEntry, StackTraceAwareEntry, XMLAwareEntry {

    private ThrowableEntry thrown;

    private String message;

    private String xmlString;

    private StackTraceElementEntry[] stackTrace;

    public XMLEntry() {
    }

    public XMLEntry(String message, Object xmlObj) throws IOException {
        setMessage(message);
        toXml(xmlObj);
    }

    public String getXmlString() {
        return xmlString;
    }

    public void setXmlString(String xmlString) {
        this.xmlString = xmlString;
    }

    public void setThrown(ThrowableEntry thrown) {
        this.thrown = thrown;
    }

    public ThrowableEntry getThrown() {
        return thrown;
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

    public StackTraceElementEntry[] getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTraceElementEntry[] stackTrace) {
        this.stackTrace = stackTrace;
    }
}
