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

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Registry for object converter.
 */
public class ObjectConverterRegistry {

    private static final ObjectConverterRegistry REGISTRY = new ObjectConverterRegistry();

    // base converter
    static {
        register(String.class, new TypeConverter<String>(String.class));
        register(Character.class, new TypeConverter<Character>(Character.class));
        register(Boolean.class, new TypeConverter<Boolean>(Boolean.class));
        register(Integer.class, new TypeConverter<Integer>(Integer.class));
        register(Short.class, new TypeConverter<Short>(Short.class));
        register(Byte.class, new TypeConverter<Byte>(Byte.class));
        register(Double.class, new TypeConverter<Double>(Double.class));
        register(Float.class, new TypeConverter<Float>(Float.class));
        register(Class.class, new TypeConverter<Class>(Class.class));

        register(Date.class, new TypeConverter<Date>(Date.class));
        register(java.sql.Date.class, new TypeConverter<java.sql.Date>(java.sql.Date.class));
        register(Time.class, new TypeConverter<Time>(Time.class));
        register(Timestamp.class, new TypeConverter<Timestamp>(Timestamp.class));
    }

    public static void register(Class clazz, ObjectConverter converter) {
        REGISTRY.add(clazz, converter);
    }

    public static boolean isSupported(Object obj) {
        return REGISTRY.isClassSupported(obj);
    }

    public static String convertToString(Object obj) {
        try {
            return REGISTRY.toString(obj);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static Object convertToObject(String className, String strValue) {
        try {
            return REGISTRY.toObject(className, strValue);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private Map<Class, ObjectConverter> converters = Collections.synchronizedMap(new LinkedHashMap<Class, ObjectConverter>());

    private void add(Class clazz, ObjectConverter converter) {
        converters.put(clazz, converter);
    }

    private boolean isClassSupported(Object obj) {
        if(obj == null) {
            throw new IllegalArgumentException("obj should not be null");
        }

        return converters.containsKey(obj.getClass());
    }

    private String toString(Object obj) throws Exception {
        if(!isClassSupported(obj)) {
            throw new IllegalArgumentException(String.format("obj '%s' not supported", String.valueOf(obj)));
        }

        ObjectConverter converter = converters.get(obj.getClass());
        return converter.convertToString(obj);
    }

    private Object toObject(String className, String strValue) throws Exception {
        Class clazz = Class.forName(className);

        if(!converters.containsKey(clazz)) {
            throw new IllegalArgumentException(String.format("className '%s' not supported", className));
        }

        ObjectConverter converter = converters.get(clazz);
        return converter.convertToObject(strValue);
    }
}
