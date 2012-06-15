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

package ard.piraso.server;

import ard.piraso.api.entry.ReferenceRequestEntry;

/**
 * Piraso entry point context preference.
 */
public class PirasoEntryPointContext implements ContextPreference {

    /**
     * Retrieves the delegate {@link PirasoContext}.
     *
     * @return the {@link PirasoContext} instance
     */
    protected PirasoContext getDelegate() {
        return PirasoContextHolder.getContext();
    }

    /**
     * {@inheritDoc}
     */
    public PirasoEntryPoint getEntryPoint() {
        return getDelegate() != null ? getDelegate().getEntryPoint() : null;
    }

    /**
     * {@inheritDoc}
     */
    public void addProperty(Class<?> clazz, String name, Object value) {
        if(getDelegate() != null) {
            getDelegate().addProperty(clazz, name, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object getProperty(Class<?> clazz, String name) {
        if(getDelegate() != null) {
            return getDelegate().getProperty(clazz, name);
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Long getRequestId() {
        return getDelegate() != null ? getDelegate().getRequestId() : null;
    }

    /**
     * {@inheritDoc}
     */
    public ReferenceRequestEntry getRef() {
        return getDelegate() != null ? getDelegate().getRef() : null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isMonitored() {
        return getDelegate() != null && getDelegate().isMonitored();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEnabled(String property) {
        return getDelegate() != null && getDelegate().isEnabled(property);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isRegexEnabled(String property) {
        return getDelegate() != null && getDelegate().isRegexEnabled(property);
    }

    /**
     * {@inheritDoc}
     */
    public Integer getIntValue(String property) {
        if(getDelegate() == null) {
            return null;
        }

        return getDelegate().getIntValue(property);
    }
}
