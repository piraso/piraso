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

package org.piraso.server;

import org.piraso.api.entry.ReferenceRequestEntry;

/**
 * The current thread context preferences.
 */
public interface ContextPreference {

    /**
     * Adds a property given the class context.
     *
     * @param clazz the class context
     * @param name the property name
     * @param value the property value
     */
    void addProperty(Class<?> clazz, String name, Object value);

    /**
     * Gets a property given the context class.
     *
     * @param clazz the class context
     * @param name the property name
     * @return the property value
     */
    Object getProperty(Class<?> clazz, String name);

    /**
     * The current piraso context request id.
     *
     * @return the request id
     */
    Long getRequestId();

    /**
     * Determines the request reference if available.
     *
     * @return {@code not null} if available, {@code null} if available.
     */
    ReferenceRequestEntry getRef();

    /**
     * Determines whether the current context request is eligible for monitoring.
     *
     * @return {@code true} if eligible, {@code false} otherwise.
     */
    boolean isMonitored();

    /**
     * Determines whether the given property is enabled or not.
     *
     * @param property the property to check.
     * @return {@code true} if enabled, {@code false} otherwise.
     */
    boolean isEnabled(String property);

    boolean isRegexEnabled(String property);

    /**
     * Determines the int property value.
     *
     * @param property the property to retrieve.
     * @return the property int value, {@code null} if not found.
     */
    Integer getIntValue(String property);

    /**
     * The piraso context entry point
     *
     * @return the piraso context entry point
     */
    PirasoEntryPoint getEntryPoint();
}
