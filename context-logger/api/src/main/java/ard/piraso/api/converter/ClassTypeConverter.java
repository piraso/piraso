package ard.piraso.api.converter;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * {@link Class} type converter.
 */
public class ClassTypeConverter implements ObjectConverter {
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * {@inheritDoc}
     */
    public String convertToString(Object obj) {
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
        try {
            return mapper.readValue(str, Class.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
