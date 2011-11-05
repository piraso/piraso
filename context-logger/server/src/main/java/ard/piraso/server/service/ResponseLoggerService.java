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

package ard.piraso.server.service;

import ard.piraso.api.Preferences;
import ard.piraso.api.entry.Entry;

import java.io.IOException;

/**
 * Defines an interface for a response logger service.
 */
public interface ResponseLoggerService {

    public User getUser();

    public String getId();

    public String getWatchedAddr();

    public Preferences getPreferences();

    public void start() throws Exception;

    public void stop() throws IOException;

    public boolean isAlive();

    public boolean isForcedStopped();

    public void stopAndWait(long timeout) throws InterruptedException, IOException;

    public void log(Entry entry) throws IOException;
}
