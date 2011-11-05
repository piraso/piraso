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

package ard.piraso.api.entry;

/**
 * An entry with elapse time.
 */
public class ElapseTimeEntry extends Entry {

    private long startTime;

    private long endTime;

    public ElapseTimeEntry() {
        this(0, 0);
    }

    public ElapseTimeEntry(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void restart() {
        this.startTime = System.currentTimeMillis();
    }

    public void start() {
        if(startTime > 0) {
            return;
        }

        this.startTime = System.currentTimeMillis();
    }

    public void stop() {
        if(startTime <= 0) {
            startTime = System.currentTimeMillis();
        }

        this.endTime = System.currentTimeMillis();
    }

    public long getElapseTime() {
        return endTime - startTime;
    }
}
