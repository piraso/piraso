package ard.piraso.server;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Test for {@link PirasoContextHolder} class.
 */
public class PirasoContextHolderTest {

    @Test(expected = IllegalAccessException.class)
    public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        PirasoContextHolder.class.newInstance();
    }

    @Test
    public void testPrivateConstructorWithAccessibility() throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Constructor constructor = PirasoContextHolder.class.getDeclaredConstructors()[0];

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        // force invoking private constructor
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testSetPirasoContext() throws Exception {
        PirasoContext context = mock(PirasoContext.class);

        PirasoContextHolder.setContext(context);
        assertSame(context, PirasoContextHolder.getContext());
    }

    @Test
    public void testRemoveContext() throws Exception {
        PirasoContext context = mock(PirasoContext.class);

        PirasoContextHolder.setContext(context);
        assertSame(context, PirasoContextHolder.getContext());

        PirasoContextHolder.removeContext();
        assertNull(PirasoContextHolder.getContext());
    }
}
