package ard.piraso.api.entry;

import ard.piraso.api.converter.ObjectConverterRegistry;

/**
 * Contains helper method for {@link ObjectEntry} class.
 *
 */
public final class ObjectEntryUtils {

    private ObjectEntryUtils() {}

    public static Object toObject(ObjectEntry entry) {
        if(entry == null || entry.isNull()) {
            return null;
        }

        if(!entry.isSupported()) {
            throw new IllegalStateException(String.format("Unsupported with class %s", entry.getClassName()));
        }

        return ObjectConverterRegistry.convertToObject(entry.getClassName(), entry.getStrValue());
    }
}
