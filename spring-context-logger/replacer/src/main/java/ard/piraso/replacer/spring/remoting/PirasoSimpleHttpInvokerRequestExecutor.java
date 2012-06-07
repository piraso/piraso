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

import ard.piraso.server.ContextPreference;
import ard.piraso.server.PirasoEntryPointContext;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;

import java.io.IOException;
import java.net.HttpURLConnection;

import static ard.piraso.api.PirasoConstants.REMOTE_ADDRESS_HEADER;
import static ard.piraso.api.PirasoConstants.REQUEST_ID_HEADER;

/**
 * Piraso aware {@link SimpleHttpInvokerRequestExecutor} instance.
 */
public class PirasoSimpleHttpInvokerRequestExecutor extends SimpleHttpInvokerRequestExecutor {

    protected ContextPreference context = new PirasoEntryPointContext();

    protected void prepareConnection(HttpURLConnection con, int contentLength) throws IOException {
        super.prepareConnection(con, contentLength);

        if(context != null && context.getEntryPoint() != null) {
            con.setRequestProperty(REMOTE_ADDRESS_HEADER, context.getEntryPoint().getRemoteAddr());
            con.setRequestProperty(REQUEST_ID_HEADER, String.valueOf(context.getRequestId()));
        }
    }
}
