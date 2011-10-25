package ard.piraso.api.entry;

import ard.piraso.api.converter.ObjectConverterRegistry;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Object entry type
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectEntry extends Entry {

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
}
