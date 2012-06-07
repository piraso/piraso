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

package ard.piraso.replacer.spring.remoting;

import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Extends {@link SimpleHttpInvokerRequestExecutor} with timeout support.
 */
public class PirasoHttpInvokerRequestExecutorWithTimeout extends PirasoSimpleHttpInvokerRequestExecutor {
    public static final int DEFAULT_CONNECT_TIMEOUT_MS = 1000;
    public static final int DEFAULT_READ_TIMEOUT_MS = 10000;

    protected int connectTimeoutMs = DEFAULT_CONNECT_TIMEOUT_MS;
    protected int readTimeoutMs = DEFAULT_READ_TIMEOUT_MS;

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public void setConnectTimeoutMs(int connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public void setReadTimeoutMs(int readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }

    protected void prepareConnection(HttpURLConnection con, int contentLength) throws IOException {
        super.prepareConnection(con, contentLength);
        con.setConnectTimeout(getConnectTimeoutMs());
        con.setReadTimeout(getReadTimeoutMs());
    }
}
