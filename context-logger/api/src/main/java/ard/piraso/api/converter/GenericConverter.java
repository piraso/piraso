package ard.piraso.api.converter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Generic entry converter.
 */
public class GenericConverter implements ObjectConverter {

    private Class clazz;

    public GenericConverter(Class clazz) {
        this.clazz = clazz;
    }

    public String convertToString(Object obj) {
        if(obj == null) {
            return null;
        }

        return String.valueOf(obj);
    }

    public Object convertToObject(String str) {
        if(str == null) {
            return null;
        }

        try {
            Method method = clazz.getMethod("valueOf", String.class);

            return  method.invoke(method, str);
        } catch (NoSuchMethodException e) {
            throw new UnsupportedOperationException(e);
        } catch (InvocationTargetException e) {
            throw new UnsupportedOperationException(e);
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
