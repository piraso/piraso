package ard.piraso.api.entry;

import ard.piraso.api.converter.ObjectConverterRegistry;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Object entry type
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectEntry implements Entry {

    private String strValue;

    private String className;

    private boolean supported;

    public ObjectEntry() {}

    public ObjectEntry(Object obj) {
        if(obj != null) {
            supported = ObjectConverterRegistry.isSupported(obj);
            className = obj.getClass().getName();

            if(supported) {
                strValue = ObjectConverterRegistry.convertToString(obj);
            }
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

    /**
     * Converts this entry to actual object instance represented.
     *
     * @return the actual object represented
     */
    public Object toObject() {
        return ObjectEntryUtils.toObject(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectEntry that = (ObjectEntry) o;

        if (supported != that.supported) return false;
        if (className != null ? !className.equals(that.className) : that.className != null) return false;
        if (strValue != null ? !strValue.equals(that.strValue) : that.strValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = strValue != null ? strValue.hashCode() : 0;
        result = 31 * result + (className != null ? className.hashCode() : 0);
        result = 31 * result + (supported ? 1 : 0);
        return result;
    }
}
