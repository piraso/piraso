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

/**
 * Class to replace
 */
public class ClassReplacement {
    private String className;

    private String replacementClassName;

    public ClassReplacement() {
    }

    public ClassReplacement(String className, String replacementClassName) {
        this.className = className;
        this.replacementClassName = replacementClassName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getReplacementClassName() {
        return replacementClassName;
    }

    public void setReplacementClassName(String replacementClassName) {
        this.replacementClassName = replacementClassName;
    }
}
