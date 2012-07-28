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

/**
 * Defines an stack trace entry.
 */
public class StackTraceElementEntry extends Entry {
    private String declaringClass;
    private String methodName;
    private String fileName;
    private int lineNumber;
    private boolean nativeMethod;

    public StackTraceElementEntry() {}

    public StackTraceElementEntry(StackTraceElement element) {
        this.declaringClass = element.getClassName();
        this.nativeMethod = element.isNativeMethod();
        this.fileName = element.getFileName();
        this.methodName = element.getMethodName();
        this.lineNumber = element.getLineNumber();
    }

    public String getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(String declaringClass) {
        this.declaringClass = declaringClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public boolean isNativeMethod() {
        return nativeMethod;
    }

    public void setNativeMethod(boolean nativeMethod) {
        this.nativeMethod = nativeMethod;
    }

    @Override
    public String toString() {
        return getDeclaringClass() + "." + methodName +
                (isNativeMethod() ? "(Native Method)" :
                        (fileName != null && lineNumber >= 0 ?
                                "(" + fileName + ":" + lineNumber + ")" :
                                (fileName != null ?  "("+fileName+")" : "(Unknown Source)")));
    }
}
