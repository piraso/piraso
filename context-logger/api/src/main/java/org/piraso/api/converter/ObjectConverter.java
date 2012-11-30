/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
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

package org.piraso.api.converter;

/**
 * Defines an interface for converting objects to string and vise versa.
 */
public interface ObjectConverter {

    /**
     * Converts the given object to string representation.
     *
     * @param obj the object to convert
     * @return the string representation of the object
     * @throws Exception on error
     */
    String convertToString(Object obj) throws Exception;

    /**
     * Converts the given string back to the object instance.
     *
     * @param str the string to convert
     * @return the object instance converted
     * @throws Exception on error
     */
    Object convertToObject(String str) throws Exception;
}
