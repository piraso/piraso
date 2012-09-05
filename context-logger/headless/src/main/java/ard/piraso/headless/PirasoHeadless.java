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

package ard.piraso.headless;

import ard.piraso.api.Preferences;
import ard.piraso.io.IOEntryManager;
import ard.piraso.io.IOEntryReader;
import ard.piraso.io.impl.HttpEntrySource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Piraso headless helps to do monitoring without using the UI based piraso client.
 * <p>
 * This is useful for automation testing.
 */
public class PirasoHeadless {

    public static PirasoHeadless create(Preferences pref, String url) {
        return create(pref, url, null);
    }

    public static PirasoHeadless create(Preferences pref, String url, String ipAddress) {
        return new PirasoHeadless(pref, url, ipAddress);
    }

    private IOEntryReader reader;

    private ExecutorService service = Executors.newSingleThreadExecutor();

    private boolean startedOnce = false;

    private PirasoHeadless(Preferences pref, String url, String ipAddress) {
        this.reader = new IOEntryReader(new HttpEntrySource(pref, url, ipAddress));
    }

    public void reset() {
        if(reader.isAlive()) {
            reader.stop();
        }

        reader = reader.createNew();
    }

    public IOEntryReader getReader() {
        return reader;
    }

    public IOEntryManager getManager() {
        return reader.getManager();
    }

    public void start() {
        if(startedOnce) {
            reset();
        }

        service.submit(new Runnable() {
            public void run() {
                startedOnce = true;
                reader.start();
            }
        });
    }

    public void stop() {
        reader.stop();
    }

    public void shutdown() {
        service.shutdownNow();
    }
}
