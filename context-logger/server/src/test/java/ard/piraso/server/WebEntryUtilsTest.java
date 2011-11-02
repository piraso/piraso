package ard.piraso.server;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static junit.framework.Assert.assertTrue;

/**
 * Test for {@link WebEntryUtils} class.
 */
public class WebEntryUtilsTest {

    @Test(expected = IllegalAccessException.class)
    public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        WebEntryUtils.class.newInstance();
    }

    @Test
    public void testPrivateConstructorWithAccessibility() throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Constructor constructor = WebEntryUtils.class.getDeclaredConstructors()[0];

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        // force invoking private constructor
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
