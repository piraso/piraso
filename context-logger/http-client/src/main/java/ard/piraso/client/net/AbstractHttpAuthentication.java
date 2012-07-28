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

package ard.piraso.client.net;

import org.apache.commons.lang.Validate;
import org.apache.http.client.HttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * Base class for authentication.
 */
public abstract class AbstractHttpAuthentication extends AbstractHttpHandler {

    protected String userName;

    protected String password;

    public AbstractHttpAuthentication(HttpClient client, HttpContext context) {
        super(client, context);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    protected void validate() {
        Validate.notNull(userName, "userName should not be null.");
        Validate.notNull(password, "password should not be null.");
        Validate.notNull(targetHost, "targetHost should not be null.");
    }

    @Override
    public void execute() throws IOException {
        validate();
        authenticate();
    }

    public abstract void authenticate();
}
