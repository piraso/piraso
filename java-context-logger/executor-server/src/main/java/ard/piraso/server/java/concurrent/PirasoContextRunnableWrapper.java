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

package ard.piraso.server.java.concurrent;

import ard.piraso.api.Level;
import ard.piraso.api.entry.RequestEntry;
import ard.piraso.api.entry.ResponseEntry;
import ard.piraso.server.GroupChainId;
import ard.piraso.server.PirasoContext;
import ard.piraso.server.PirasoContextHolder;
import ard.piraso.server.dispatcher.ContextLogDispatcher;

/**
 * runnable wrapper
 */
public class PirasoContextRunnableWrapper implements Runnable {

    private Runnable delegate;

    private PirasoContext context;

    private RequestEntry request;

    private ResponseEntry response;

    public PirasoContextRunnableWrapper(Runnable delegate, PirasoContext context, RequestEntry request, ResponseEntry response) {
        this.delegate = delegate;
        this.context = context;
        this.request = request;
        this.response = response;
    }

    public void run() {
        PirasoContextHolder.setContext(context);

        ContextLogDispatcher.forward(Level.SCOPED, new GroupChainId("request"), request);

        try {
            delegate.run();
        } finally {
            ContextLogDispatcher.forward(Level.SCOPED, new GroupChainId("response"), response);
            PirasoContextHolder.removeContext();
        }
    }
}