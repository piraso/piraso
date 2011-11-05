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
 * Http request context preference.
 */
public class PirasoRequestContext implements ContextPreference {

    /**
     * The delegate instance
     */
    private PirasoContext delegate;

    /**
     * Construct this request context
     */
    public PirasoRequestContext() {
        this.delegate = PirasoContextHolder.getContext();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isMonitored() {
        if(delegate == null) {
            return false;
        }

        return delegate.isMonitored();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEnabled(String property) {
        if(delegate == null) {
            return false;
        }

        return delegate.isEnabled(property);
    }

    /**
     * {@inheritDoc}
     */
    public Integer getIntValue(String property) {
        if(delegate == null) {
            return null;
        }

        return delegate.getIntValue(property);
    }

    /**
     *  {@inheritDoc}
     */
    public void requestOnScope() {
        if(delegate != null) {
            delegate.requestOnScope();
        }
    }
}
