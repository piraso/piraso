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

package org.piraso.api.entry;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Defines a base entry class.
 */
public class Entry {

    protected Long requestId;

    protected Long baseRequestId;

    protected String level;

    protected GroupEntry referenceGroup;

    protected GroupEntry group;

    protected int indent = 0;

    public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    public Long getRequestId() {
        return requestId;
    }

    public GroupEntry getReferenceGroup() {
        return referenceGroup;
    }

    public void setReferenceGroup(GroupEntry referenceGroup) {
        this.referenceGroup = referenceGroup;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getBaseRequestId() {
        return baseRequestId;
    }

    public void setBaseRequestId(Long baseRequestId) {
        this.baseRequestId = baseRequestId;
    }

    public GroupEntry getGroup() {
        return group;
    }

    public void setGroup(GroupEntry group) {
        this.group = group;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
