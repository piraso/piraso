package ard.piraso.api.converter;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Generic Bean converter.
 * <p>
 * Warning:
 * Ensure that no bi-directional relationship for associated beans, otherwise
 * this will cause {@link StackOverflowError}.
 */
public class BeanConverter<T> implements ObjectConverter {
    private final ObjectMapper mapper;

    private Class<T> clazz;

    /**
     * Create a bean converter given the {@link Class}.
     *
     * @param clazz {@link Class} to be converted to and from
     */
    public BeanConverter(Class<T> clazz) {
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
