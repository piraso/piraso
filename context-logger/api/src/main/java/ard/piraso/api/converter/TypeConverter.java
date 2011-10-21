package ard.piraso.api.converter;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Generic type converter. This can also be used for bean type classes.
 * <p>
 * For beans ensure to register only those that do not have any bi-directional relationship for associations.
 */
public class TypeConverter<T> implements ObjectConverter {

    private final ObjectMapper mapper;

    private Class<T> clazz;

    public TypeConverter(Class<T> clazz) {
        this.clazz = clazz;

        mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public String convertToString(Object obj) {
        if(obj == null) {
            return null;
        }

        if(!clazz.isInstance(obj)) {
            throw new IllegalStateException(String.format("obj argument is not of type '%s' was '%s'.",
                    clazz.getName(), obj.getClass().getName()));
        }

        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object convertToObject(String str) {
        if(str == null) {
            return null;
        }

        try {
            return mapper.readValue(str, clazz);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
