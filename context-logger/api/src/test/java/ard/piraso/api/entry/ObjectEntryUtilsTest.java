package ard.piraso.api.entry;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static junit.framework.Assert.*;

/**
 * Test for {@link ObjectEntryUtils} class.
 */
public class ObjectEntryUtilsTest {

    @Test(expected = IllegalAccessException.class)
    public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        ObjectEntryUtils.class.newInstance();
    }

    @Test
    public void testPrivateConstructorWithAccessibility() throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Constructor constructor = ObjectEntryUtils.class.getDeclaredConstructors()[0];

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        // force invoking private constructor
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testToObjectNull() {
        assertNull(ObjectEntryUtils.toObject(null));
        assertNull(ObjectEntryUtils.toObject(new ObjectEntry(null)));
    }

    @Test(expected = IllegalStateException.class)
    public void testToObjectNotSupported() {
        ObjectEntryUtils.toObject(new ObjectEntry(new StringBuffer()));
    }

    @Test
    public void testToObjectValid() throws Exception {
        String test = "test";
        ObjectEntry entry = new ObjectEntry("test");

        assertEquals(ObjectEntryUtils.toObject(entry), test);
    }
}
