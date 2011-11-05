/*
 * Copyright (c) 2011. Piraso Alvin R. de Leon. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The Piraso licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ard.piraso.api;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Monitor preferences.
 */
public class Preferences {

    private Map<String, Property<Boolean>> booleanProperties;

    private Map<String, Property<Integer>> integerProperties;

    private List<String> urlPatterns;

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

    public void addUrlPattern(String pattern) {
        if(urlPatterns == null) {
            urlPatterns = new ArrayList<String>();
        }

        urlPatterns.add(pattern);
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

    public List<String> getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(List<String> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    public boolean isUrlAcceptable(String url) {
        // if empty all url are acceptable
        if(CollectionUtils.isEmpty(urlPatterns)) {
            return true;
        }

        for(String pattern : urlPatterns) {
            if(url.matches(pattern)) {
                return true;
            }
        }

        return false;
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
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
