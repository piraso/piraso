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

package ard.piraso.io.cache;

import java.io.Serializable;

/**
 * Cache key
 */
public class IOEntryCacheKey implements Serializable {
    private String id;

    private long requestId;

    private long rowNum;

    public IOEntryCacheKey(String id, long requestId, long rowNum) {
        this.id = id;
        this.requestId = requestId;
        this.rowNum = rowNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IOEntryCacheKey that = (IOEntryCacheKey) o;

        if (requestId != that.requestId) return false;
        if (rowNum != that.rowNum) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (requestId ^ (requestId >>> 32));
        result = 31 * result + (int) (rowNum ^ (rowNum >>> 32));
        return result;
    }
}
