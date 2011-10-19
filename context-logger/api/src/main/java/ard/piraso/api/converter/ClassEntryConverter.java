package ard.piraso.api.converter;

/**
 * Converter for {@link Class} types.
 */
public class ClassEntryConverter implements ObjectConverter {
    public String convertToString(Object obj) {
        if(obj == null) {
            return null;
        }

        return ((Class) obj).getName();
    }

    public Object convertToObject(String str) {
        if(str == null) {
            return null;
        }

        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
