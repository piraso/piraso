package ard.piraso.api.entry;

import ard.piraso.api.AbstractJacksonTest;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link MethodCallEntry} class.
 */
public class MethodCallEntryTest extends AbstractJacksonTest {

    @Test
    public void testJacksonNoException() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = Integer.class.getMethod("valueOf", new Class[] {String.class});
        Object[] arguments = new Object[] {"13"};

        ElapseTimeEntry expectedElapseTime = new ElapseTimeEntry(System.currentTimeMillis(), System.currentTimeMillis() + 3000l);
        MethodCallEntry expectedMethodCall = new MethodCallEntry(method, expectedElapseTime);
        expectedMethodCall.setArguments(new ObjectEntry[] {new ObjectEntry("13")});
        expectedMethodCall.setReturnedValue(new ObjectEntry(method.invoke(Integer.class, arguments)));

        String jsonValue = mapper.writeValueAsString(expectedMethodCall);
        MethodCallEntry actualMethodCall = mapper.readValue(jsonValue, MethodCallEntry.class);

        assertThat("not null elapse time", actualMethodCall.getElapseTime(), CoreMatchers.<Object>notNullValue());
        assertThat("not null method name", actualMethodCall.getMethodName(), CoreMatchers.<Object>notNullValue());
        assertThat("not null arguments", actualMethodCall.getArguments(), CoreMatchers.<Object>notNullValue());
        assertThat("not null parameter class names", actualMethodCall.getParameterClassNames(), CoreMatchers.<Object>notNullValue());
        assertThat("not null return class names", actualMethodCall.getReturnClassName(), CoreMatchers.<Object>notNullValue());
        assertThat("not null return value", actualMethodCall.getReturnedValue(), CoreMatchers.<Object>notNullValue());
        assertThat("null stack trace", actualMethodCall.getStackTrace(), CoreMatchers.<Object>nullValue());
        assertThat("null throwable", actualMethodCall.getThrown(), CoreMatchers.<Object>nullValue());
        assertThat("same entry", actualMethodCall, is(expectedMethodCall));
    }

    @Test
    public void testJacksonWithStackTrace() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = Integer.class.getMethod("valueOf", new Class[] {String.class});
        Object[] arguments = new Object[] {"13"};

        ElapseTimeEntry expectedElapseTime = new ElapseTimeEntry(System.currentTimeMillis(), System.currentTimeMillis() + 3000l);
        MethodCallEntry expectedMethodCall = new MethodCallEntry(method, expectedElapseTime);
        expectedMethodCall.setArguments(new ObjectEntry[] {new ObjectEntry("13")});
        expectedMethodCall.setReturnedValue(new ObjectEntry(method.invoke(Integer.class, arguments)));

        // stack trace
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StackTraceElementEntry[] stackTrace = new StackTraceElementEntry[elements.length];

        for(int i = 0; i < elements.length; i++) {
            stackTrace[i] = new StackTraceElementEntry(elements[i]);
        }

        expectedMethodCall.setStackTrace(stackTrace);

        String jsonValue = mapper.writeValueAsString(expectedMethodCall);
        MethodCallEntry actualMethodCall = mapper.readValue(jsonValue, MethodCallEntry.class);

        assertThat("not null elapse time", actualMethodCall.getElapseTime(), CoreMatchers.<Object>notNullValue());
        assertThat("not null method name", actualMethodCall.getMethodName(), CoreMatchers.<Object>notNullValue());
        assertThat("not null arguments", actualMethodCall.getArguments(), CoreMatchers.<Object>notNullValue());
        assertThat("not null parameter class names", actualMethodCall.getParameterClassNames(), CoreMatchers.<Object>notNullValue());
        assertThat("not null return class names", actualMethodCall.getReturnClassName(), CoreMatchers.<Object>notNullValue());
        assertThat("not null return value", actualMethodCall.getReturnedValue(), CoreMatchers.<Object>notNullValue());
        assertThat("not null stack trace", actualMethodCall.getStackTrace(), CoreMatchers.<Object>notNullValue());
        assertThat("null throwable", actualMethodCall.getThrown(), CoreMatchers.<Object>nullValue());
        assertThat("same entry", actualMethodCall, is(expectedMethodCall));
    }

    @Test
    public void testJacksonWithException() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = ClassWithException.class.getMethod("methodWrapRethrown", new Class[0]);

        ElapseTimeEntry expectedElapseTime = new ElapseTimeEntry(System.currentTimeMillis(), System.currentTimeMillis() + 3000l);
        MethodCallEntry expectedMethodCall = new MethodCallEntry(method, expectedElapseTime);
        expectedMethodCall.setArguments(new ObjectEntry[0]);

        try {
            new ClassWithException().methodWrapRethrown();
        } catch(IllegalStateException e) {
            expectedMethodCall.setThrown(new ThrowableEntry(e));
        }

        String jsonValue = mapper.writeValueAsString(expectedMethodCall);
        MethodCallEntry actualMethodCall = mapper.readValue(jsonValue, MethodCallEntry.class);

        assertThat("not null elapse time", actualMethodCall.getElapseTime(), CoreMatchers.<Object>notNullValue());
        assertThat("not null method name", actualMethodCall.getMethodName(), CoreMatchers.<Object>notNullValue());
        assertThat("not null arguments", actualMethodCall.getArguments(), CoreMatchers.<Object>notNullValue());
        assertThat("not null return class names", actualMethodCall.getReturnClassName(), CoreMatchers.<Object>notNullValue());
        assertThat("not null throwable", actualMethodCall.getThrown(), CoreMatchers.<Object>notNullValue());

        assertThat("null parameter class names", actualMethodCall.getParameterClassNames(), CoreMatchers.<Object>nullValue());
        assertThat("null return value", actualMethodCall.getReturnedValue(), CoreMatchers.<Object>nullValue());
        assertThat("null stack trace", actualMethodCall.getStackTrace(), CoreMatchers.<Object>nullValue());
        assertThat("same entry", actualMethodCall, is(expectedMethodCall));
    }

    private static class ClassWithException {

        public void methodWrapRethrown() {
            try {
                methodWithException();
            } catch(IllegalArgumentException e) {
                throw new IllegalStateException(e);
            }
        }

        public void methodWithException() {
            throw new IllegalArgumentException();
        }
    }
}
