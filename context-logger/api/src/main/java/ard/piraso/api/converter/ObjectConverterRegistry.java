package ard.piraso.api.converter;

import java.util.Collections;
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
        register(Class.class, new ClassTypeConverter());
    }

    public static void register(Class clazz, ObjectConverter converter) {
        REGISTRY.add(clazz, converter);
    }

    public static boolean isSupported(Object obj) {
        return REGISTRY.isClassSupported(obj);
    }

    public static String convertToString(Object obj) {
        return REGISTRY.toString(obj);
    }

    public static Object convertToObject(String className, String strValue) {
        try {
            return REGISTRY.toObject(className, strValue);
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException(e);
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
