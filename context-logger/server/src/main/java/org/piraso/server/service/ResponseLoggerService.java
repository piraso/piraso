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

import org.piraso.api.Preferences;
import org.piraso.api.entry.Entry;

import java.io.IOException;

/**
 * Defines an interface for a response logger service.
 */
public interface ResponseLoggerService {

    public User getUser();

    public String getId();

    public Long getGlobalId();

    public String getWatchedAddr();

    public boolean isWatched(String remoteAddr);

    public Preferences getPreferences();

    public void start() throws Exception;

    public void stop() throws IOException;

    public boolean isAlive();

    public boolean isForcedStopped();

    public void stopAndWait(long timeout) throws InterruptedException, IOException;

    public void log(Entry entry) throws IOException;

    public void addStopListener(StopLoggerListener listener);

    public void removeStopListener(StopLoggerListener listener);

    public void fireStopEvent(StopLoggerEvent event);
}
