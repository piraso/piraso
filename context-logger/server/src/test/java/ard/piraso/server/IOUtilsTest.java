package ard.piraso.server;

import org.junit.Test;

import java.io.Closeable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link IOUtils} class.
 */
public class IOUtilsTest {

    @Test(expected = IllegalAccessException.class)
    public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        IOUtils.class.newInstance();
    }

    @Test
    public void testPrivateConstructorWithAccessibility() throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Constructor constructor = IOUtils.class.getDeclaredConstructors()[0];

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        // force invoking private constructor
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testCloseQuitelyNull() throws Exception {
        IOUtils.closeQuitely(null);
    }

    @Test
    public void testCloseQuitelyNotNull() throws Exception {
        Closeable closeable = mock(Closeable.class);

        IOUtils.closeQuitely(closeable);

        verify(closeable).close();
    }
}
