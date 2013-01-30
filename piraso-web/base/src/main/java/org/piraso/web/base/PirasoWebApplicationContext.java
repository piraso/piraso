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

package org.piraso.web.base;

import org.piraso.server.ContextLoggerBeanProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Piraso aware {@link XmlWebApplicationContext} class.
 * This supports cascading of {@link org.piraso.server.ContextLoggerBeanProcessor} to child contexts.
 */
public class PirasoWebApplicationContext extends XmlWebApplicationContext {

    @Override
    protected DefaultListableBeanFactory createBeanFactory() {
        DefaultListableBeanFactory factory = super.createBeanFactory();

        if(getParent() != null) {
            String[] postProcessorNames = getParent().getBeanNamesForType(ContextLoggerBeanProcessor.class, true, false);

            for (String ppName : postProcessorNames) {
                BeanPostProcessor pp = getParent().getBean(ppName, BeanPostProcessor.class);
                factory.addBeanPostProcessor(pp);
            }
        }

        return factory;
    }
}
