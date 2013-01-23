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

package org.piraso.server.service;

import org.piraso.server.PirasoRequest;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.UUID;

/**
 * Represents a piraso user. This user monitors context logs.
 */
public class User {
    public String remoteAddr;

    public String activityUuid;

    public User() {
    }

    public User(String remoteAddr, String activityUuid) {
        this.remoteAddr = remoteAddr;
        this.activityUuid = activityUuid;
    }

    public User(PirasoRequest request) {
        remoteAddr = request.getRemoteAddr();

        if(request.getActivityUuid() != null) {
            activityUuid = request.getActivityUuid();
        } else {
            activityUuid = UUID.randomUUID().toString();
        }
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public void setActivityUuid(String activityUuid) {
        this.activityUuid = activityUuid;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public String getActivityUuid() {
        return activityUuid;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
