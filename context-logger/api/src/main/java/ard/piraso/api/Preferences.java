package ard.piraso.api;

import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * User preferences.
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
        if(MapUtils.isEmpty(integerProperties) || integerProperties.containsKey(property)) {
            return null;
        }

        return integerProperties.get(property).getValue();
    }
}
