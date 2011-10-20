package ard.piraso.api;

import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Monitor preferences.
 */
public class Preferences {

    private Map<String, Property<Boolean>> booleanProperties;

    private Map<String, Property<Integer>> integerProperties;

    public Map<String, Property<Boolean>> getBooleanProperties() {
        return booleanProperties;
    }

    public void addProperty(String name, boolean value) {
        if(MapUtils.isEmpty(booleanProperties)) {
            booleanProperties = new HashMap<String, Property<Boolean>>();
        }

        Property<Boolean> property = new Property<Boolean>(name, value);

        booleanProperties.put(property.getName(), property);
    }

    public void addProperty(String name, int value) {
        if(MapUtils.isEmpty(integerProperties)) {
            integerProperties = new HashMap<String, Property<Integer>>();
        }

        Property<Integer> property = new Property<Integer>(name, value);

        integerProperties.put(property.getName(), property);
    }

    public void setBooleanProperties(Map<String, Property<Boolean>> booleanProperties) {
        this.booleanProperties = booleanProperties;
    }

    public Map<String, Property<Integer>> getIntegerProperties() {
        return integerProperties;
    }

    public void setIntegerProperties(Map<String, Property<Integer>> integerProperties) {
        this.integerProperties = integerProperties;
    }

    public boolean isEnabled(String property) {
        return MapUtils.isNotEmpty(booleanProperties) && booleanProperties.containsKey(property) && booleanProperties.get(property).getValue();
    }

    public Integer getIntValue(String property) {
        if(MapUtils.isEmpty(integerProperties) || !integerProperties.containsKey(property)) {
            return null;
        }

        return integerProperties.get(property).getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Preferences that = (Preferences) o;

        if (booleanProperties != null ? !booleanProperties.equals(that.booleanProperties) : that.booleanProperties != null)
            return false;
        if (integerProperties != null ? !integerProperties.equals(that.integerProperties) : that.integerProperties != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = booleanProperties != null ? booleanProperties.hashCode() : 0;
        result = 31 * result + (integerProperties != null ? integerProperties.hashCode() : 0);
        return result;
    }
}
