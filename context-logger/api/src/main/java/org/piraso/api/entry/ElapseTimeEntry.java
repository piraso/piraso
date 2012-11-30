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

    public String prettyPrint() {
        long time = getElapseTime();
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;

        if(time > 0) {
            days = time / (1000 * 60 * 60 * 24);
            time -= days * (1000 * 60 * 60 * 24);
        }
        if(time > 0) {
            hours = time / (1000 * 60 * 60);
            time -= hours * (1000 * 60 * 60);
        }
        if(time > 0) {
            minutes = time / (1000 * 60);
            time -= minutes * (1000 * 60);
        }
        if(time > 0) {
            seconds = time / 1000;
            time -= seconds * 1000;
        }

        StringBuilder buf = new StringBuilder();

        if(days > 0)    buf.append(days).append("d");
        if(hours > 0)   buf.append(buf.length() > 0 ? " " : "").append(hours).append("h");
        if(minutes > 0) buf.append(buf.length() > 0 ? " " : "").append(minutes).append("m");
        if(seconds > 0) buf.append(buf.length() > 0 ? " " : "").append(seconds).append("s");
        if(time > 0)    buf.append(buf.length() > 0 ? " " : "").append(time).append("ms");

        if(buf.length() <= 0) {
            buf.append("0ms");
        }

        return buf.toString();
    }
}
