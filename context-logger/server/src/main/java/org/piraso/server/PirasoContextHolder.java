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

import org.apache.commons.lang.Validate;

/**
 * This will contain the {@link PirasoContext} instance for the current thread.
 */
public class PirasoContextHolder {

    /**
     * Thread context holder.
     */
    private static final ThreadLocal<PirasoContext> pirasoContextHolder = new ThreadLocal<PirasoContext>();

    /**
     * Sets the current {@link PirasoContext} thread instance.
     *
     * @param context the set thread instance
     */
    public static void setContext(PirasoContext context) {
        Validate.notNull(context, "'context' should not be null.");

        pirasoContextHolder.set(context);
    }

    /**
     * Remove the current {@link PirasoContext} thread instance.
     */
    public static void removeContext() {
        pirasoContextHolder.remove();
    }

    /**
     * Retrieves the current  {@link PirasoContext} thread instance.
     *
     * @return the current  {@link PirasoContext} thread instance.
     */
    public static PirasoContext getContext() {
        return pirasoContextHolder.get();
    }

    /**
     * Don't let anybody instantiate this class.
     */
    private PirasoContextHolder() {}
}
