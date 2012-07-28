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

package ard.piraso.server.log4j;

import ard.piraso.api.PirasoLogger;
import ard.piraso.server.log4j.logger.RepositorySelectorProxyFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.spi.RepositorySelector;

import java.lang.reflect.Field;

/**
 * Context logger
 */
public class Log4jContextLogger {

    /**
     * final class logging instance.
     */
    private static final Log LOG = LogFactory.getLog(Log4jContextLogger.class);

    public void init() {
        try {
            RepositorySelector selector = (RepositorySelector) getPrivateField(LogManager.class, "repositorySelector");
            Object guard = getPrivateField(LogManager.class, "guard");

            LogManager.setRepositorySelector(new RepositorySelectorProxyFactory().getProxy(selector), guard);

            // proxy entry for log4j
            if(PirasoLogger.getProxyEntry().isInfoEnabled()) {
                PirasoLogger.getProxyEntry().info("[PIRASO PROXY ENTRY]: LOG4J proxy enabled.");
            }
        } catch(Exception e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    /**
     * Returns the value of the field named "fieldName", on the specified object.
     * The value is automatically wrapped in an object if it has a primitive type.
     *
     * @param clazz object from which the represented field's value is to be extracted
     * @param fieldName name of the field contained on obj object
     * @return the value of the represented field in object obj; primitive values are wrapped in an appropriate object before being returned
     * @throws Exception on error
     */
    public static Object getPrivateField(Class clazz, String fieldName) throws Exception {
        final Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);

        return field.get(clazz);
    }
}
