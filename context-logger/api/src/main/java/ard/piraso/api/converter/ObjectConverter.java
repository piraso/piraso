package ard.piraso.api.converter;

/**
 * Defines an interface for converting objects to string and vise versa.
 */
public interface ObjectConverter {

    /**
     * Converts the given object to string representation.
     *
     * @param obj the object to convert
     * @return the string representation of the object
     */
    String convertToString(Object obj);

    /**
     * Converts the given string back to the object instance.
     *
     * @param str the string to convert
     * @return the object instance converted
     */
    Object convertToObject(String str);
}
