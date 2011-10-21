package ard.piraso.api.converter;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;

/**
 * Generic type converter.
 * <p>
 * This is used to convert primitive wrapper types.
 */
public class TypeConverter<T> implements ObjectConverter {

    private final ObjectMapper mapper = new ObjectMapper();

    private Class<T> clazz;

    public TypeConverter(Class<T> clazz) {
        this.clazz = clazz;
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
            return mapper.writeValueAsString(new TypeHolder<T>((T) obj));
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
            TypeHolder<T> holder = mapper.readValue(str, new TypeReference<TypeHolder<T>>() {});

            return holder.value;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static class TypeHolder<T> {
        protected T value;

        public TypeHolder() {}

        public TypeHolder(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}
