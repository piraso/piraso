package ard.piraso.api.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry for object converter registry.
 */
public class ObjectConverterRegistry {

    private static ObjectConverterRegistry registry = new ObjectConverterRegistry();

    // base converter
    static {
        register(String.class, new GenericConverter<String>(String.class));
        register(Integer.class, new GenericConverter<Integer>(Integer.class));
        register(Short.class, new GenericConverter<Short>(Short.class));
        register(Byte.class, new GenericConverter<Byte>(Byte.class));
        register(Double.class, new GenericConverter<Double>(Double.class));
        register(Float.class, new GenericConverter<Float>(Float.class));
        register(Class.class, new ClassEntryConverter());
    }

    public static void register(Class clazz, ObjectConverter converter) {
        registry.add(clazz, converter);
    }

    public static boolean isSupported(Object obj) {
        return registry.isClassSupported(obj);
    }

    public static String convertToString(Object obj) {
        return registry.toString(obj);
    }

    public static Object convertToObject(String className, String strValue) {
        try {
            return registry.toObject(className, strValue);
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private Map<Class, ObjectConverter> converters = Collections.synchronizedMap(new HashMap<Class, ObjectConverter>());

    private void add(Class clazz, ObjectConverter converter) {
        converters.put(clazz, converter);
    }

    private boolean isClassSupported(Object obj) {
        if(obj == null) {
            throw new IllegalArgumentException("obj should not be null");
        }

        return converters.containsKey(obj.getClass());
    }

    private String toString(Object obj) {
        if(!isClassSupported(obj)) {
            throw new IllegalArgumentException(String.format("obj '%s' not supported", String.valueOf(obj)));
        }

        ObjectConverter converter = converters.get(obj.getClass());
        return converter.convertToString(obj);
    }

    private Object toObject(String className, String strValue) throws ClassNotFoundException {
        Class clazz = Class.forName(className);

        if(!converters.containsKey(clazz)) {
            throw new IllegalArgumentException(String.format("className '%s' not supported", className));
        }

        ObjectConverter converter = converters.get(clazz);
        return converter.convertToObject(strValue);
    }
}
