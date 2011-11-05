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

package ard.piraso.server;

/**
 * The current thread context preferences.
 */
public interface ContextPreference {

    /**
     * Determines whether the current context request is eligible for monitoring.
     *
     * @return {@code true} if eligible, {@code false} otherwise.
     */
    public boolean isMonitored();

    /**
     * Determines whether the given property is enabled or not.
     *
     * @param property the property to check.
     * @return {@code true} if enabled, {@code false} otherwise.
     */
    public boolean isEnabled(String property);

    /**
     * Determines the int property value.
     *
     * @param property the property to retrieve.
     * @return the property int value, {@code null} if not found.
     */
    public Integer getIntValue(String property);

    /**
     * When invoked, this determines that the current request is executed on a logging scoped.
     * <p>
     * For sql context logging, only request with transactional scope will trigger this. Any request that uses {@link javax.sql.DataSource} will
     * invoke this method.
     * <p>
     * This will ensure that request that only do resource serving will be ignored.
     */
    public void requestOnScope();
}
