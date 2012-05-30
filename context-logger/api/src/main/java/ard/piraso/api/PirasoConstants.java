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

package ard.piraso.api;

/**
 * Constants
 */
public final class PirasoConstants {
    /**
     * Request parameter name for the remote monitored address.
     */
    public static final String WATCHED_ADDR_PARAMETER = "watchedAddr";

    /**
     * Request parameter name for the logging preferences.
     */
    public static final String PREFERENCES_PARAMETER = "preferences";

    /**
     * Request parameter name for the logging preferences.
     */
    public static final String ACTIVITY_PARAMETER = "activity_uuid";

    public static final String SERVICE_PARAMETER = "service";

    public static final String SERVICE_START_PARAMETER_VALUE = "start";

    public static final String SERVICE_STOP_PARAMETER_VALUE = "stop";

    public static final String SERVICE_TEST_PARAMETER_VALUE = "test";

    public static final String ENCODING_UTF_8 = "UTF-8";

    public static final String STATUS_OK = "SUCCESS";

    public static final String XML_CONTENT_TYPE = "text/xml";

    public static final String JSON_CONTENT_TYPE = "application/json";

    //--- These are the cascaded properties for call propagation.

    public static String REMOTE_ADDRESS_HEADER = "Piraso-Remote-Address";

    public static String REQUEST_ID_HEADER = "Piraso-Request-Id";

    public static String GROUP_ID_HEADER = "Piraso-Group-Id";

}
