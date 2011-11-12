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

package ard.piraso.api.converter;

import ard.piraso.api.JacksonUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Generic type converter. This can also be used for bean type classes.
 * <p>
 * For beans ensure to register only those that do not have any bi-directional relationship for associations.
 */
public class TypeConverter<T> implements ObjectConverter {

    private final ObjectMapper mapper;

    private Class<T> clazz;

    public TypeConverter(Class<T> clazz) {
        this.clazz = clazz;

        mapper = JacksonUtils.createMapper();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public String convertToString(Object obj) throws IOException {
        if(obj == null) {
            return null;
        }

        if(!clazz.isInstance(obj)) {
            throw new IllegalArgumentException(String.format("obj argument is not of type '%s' was '%s'.",
                    clazz.getName(), obj.getClass().getName()));
        }

        return mapper.writeValueAsString(obj);
    }

    /**
     * {@inheritDoc}
     */
    public Object convertToObject(String str) throws IOException {
        if(str == null) {
            return null;
        }

        return mapper.readValue(str, clazz);
    }
}
