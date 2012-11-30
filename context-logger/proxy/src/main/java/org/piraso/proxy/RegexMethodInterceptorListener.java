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

package org.piraso.proxy;

/**
 * Regular expression method interceptor listener.
 */
public interface RegexMethodInterceptorListener<T> {
    /**
     * Will be triggered before a target method is invoked.
     *
     * @param evt the event object
     */
    public void beforeCall(RegexMethodInterceptorEvent<T> evt);

    /**
     * Will be triggered after a target method is invoked.
     *
     * @param evt the event object
     */
    public void afterCall(RegexMethodInterceptorEvent<T> evt);

    /**
     * Will be triggered after a target method is invoked and when an exception is thrown.
     *
     * @param evt the event object
     */
    public void exceptionCall(RegexMethodInterceptorEvent<T> evt);
}
