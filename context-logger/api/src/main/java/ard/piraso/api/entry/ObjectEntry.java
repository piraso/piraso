package ard.piraso.api.entry;

import ard.piraso.api.converter.ObjectConverterRegistry;

/**
 * Object entry type
 */
public class ObjectEntry implements Entry {

    public static Object toObject(ObjectEntry entry) {
        if(entry == null || entry.isNull()) {
            return null;
        }

        if(!entry.isSupported()) {
            throw new IllegalStateException(String.format("Unsupported with class %s", entry.getClassName()));
        }

        return ObjectConverterRegistry.convertToObject(entry.getClassName(), entry.getStrValue());
    }

    private String strValue;

    private String className;

    private boolean supported;

    public ObjectEntry() {}

    public ObjectEntry(Object obj) {
        if(obj == null) {
            return;
        }

        supported = ObjectConverterRegistry.isSupported(obj);
        className = obj.getClass().getName();

        if(supported) {
            strValue = ObjectConverterRegistry.convertToString(obj);
        }
    }

    public boolean isNull() {
        return className == null;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isSupported() {
        return supported;
    }

    public void setSupported(boolean supported) {
        this.supported = supported;
    }
}
