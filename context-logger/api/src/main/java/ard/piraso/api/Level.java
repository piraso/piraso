package ard.piraso.api;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Log level preference
 */
public class Level {

    private static final String ALL_LEVEL = "ALL";

    private static final String SCOPE_LEVEL = GeneralPreferenceEnum.SCOPE_ENABLED.getPropertyName();

    private static final Map<String, Level> LEVELS = new HashMap<String, Level>() {{
        put(ALL_LEVEL, new Level(ALL_LEVEL));
        put(SCOPE_LEVEL, new Level(SCOPE_LEVEL));
    }};

    public static Level ALL = Level.get(ALL_LEVEL);

    public static Level SCOPED = Level.get(SCOPE_LEVEL);

    public static Level get(String name) {
        return LEVELS.get(name);
    }

    public static boolean isLevel(String name) {
        return LEVELS.containsKey(name);
    }

    public static void addLevel(String name) {
        if(LEVELS.containsKey(name)) {
            throw new IllegalArgumentException(String.format("Level with name '%s' already exists.", name));
        }

        LEVELS.put(name, new Level(name));
    }

    private final String name;

    private Level(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
