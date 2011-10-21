package ard.piraso.api.entry;

import ard.piraso.api.AbstractJacksonTest;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

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
        MethodCallEntry expectedMethodCall = new MethodCallEntry(method);
        expectedMethodCall.setElapseTime(expectedElapseTime);
        expectedMethodCall.setArguments(EntryUtils.toEntry(arguments));
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
        expectedMethodCall.setStackTrace(EntryUtils.toEntry(Thread.currentThread().getStackTrace()));

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

    @Test
    public void testHashCode() throws NoSuchMethodException {
        Method method = ClassWithException.class.getMethod("methodWrapRethrown", new Class[0]);
        Method method2 = Integer.class.getMethod("valueOf", new Class[] {String.class});


        MethodCallEntry e1 = new MethodCallEntry(method);
        MethodCallEntry e2 = new MethodCallEntry(method2) {{
            setElapseTime(new ElapseTimeEntry());
        }};

        MethodCallEntry e3 = new MethodCallEntry(method);
        MethodCallEntry e4 = new MethodCallEntry();

        Set<MethodCallEntry> set = new HashSet<MethodCallEntry>();
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);

        // should only be 2 since e3 and e1 is same
        assertThat(set.size(), is(3));
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
