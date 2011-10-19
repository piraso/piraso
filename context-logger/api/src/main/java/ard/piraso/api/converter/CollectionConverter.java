package ard.piraso.api.converter;

/**
 * Collection converter
 */
public class CollectionConverter implements ObjectConverter {

    private Class clazz;

    public CollectionConverter(Class clazz) {
        this.clazz = clazz;
    }

    public String convertToString(Object obj) {
        return null;
    }

    public Object convertToObject(String str) {
        return null;
    }
}
