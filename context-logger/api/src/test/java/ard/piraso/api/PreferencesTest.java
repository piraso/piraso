package ard.piraso.api;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link Preferences} class.
 */
public class PreferencesTest extends AbstractJacksonTest {

    @Test
    public void testAddProperty() throws Exception {
        Preferences preferences = new Preferences();

        preferences.addProperty("1", 1);
        preferences.addProperty("true", true);
        preferences.addProperty(GeneralPreferenceEnum.MONITOR_SELF.getPropertyName(), true);

        assertThat(preferences.isEnabled("true"), is(true));
        assertThat(preferences.isEnabled(GeneralPreferenceEnum.MONITOR_SELF.getPropertyName()), is(true));
        assertThat(preferences.isEnabled("not existing"), is(false));
        assertThat(preferences.getIntValue("1"), is(1));
        assertThat(preferences.getIntValue("not existsing"), CoreMatchers.<Object>nullValue());
    }

    @Test
    public void testIsEnabledEmptyMap() throws Exception {
        Preferences preferences = new Preferences();

        assertFalse(preferences.isEnabled("true"));
    }

    @Test
    public void testIsEnabledNotValidProperty() throws Exception {
        Preferences preferences = new Preferences();

        preferences.addProperty("false", false);

        assertFalse(preferences.isEnabled("true"));
        assertFalse(preferences.isEnabled("false"));
    }

    @Test
    public void testGetIntValueEmptyMap() throws Exception {
        Preferences preferences = new Preferences();

        assertNull(preferences.getIntValue("1"));
    }

    @Test
    public void testAddUrlPattern() throws Exception {
        Preferences preferences = new Preferences();

        assertTrue(preferences.isUrlAcceptable("/test"));

        preferences.addUrlPattern("/test");

        assertTrue(preferences.isUrlAcceptable("/test"));
        assertFalse(preferences.isUrlAcceptable("/test2"));
    }

    @Test
    public void testJackson() throws Exception {
        Preferences expected = new Preferences();

        expected.addProperty("1", 1);
        expected.addProperty("true", true);
        expected.addProperty("2", 2);
        expected.addProperty("false", false);
        expected.addProperty(GeneralPreferenceEnum.STACK_TRACE_ENABLED.getPropertyName(), false);
        expected.addUrlPattern(".*");

        String jsonValue = mapper.writeValueAsString(expected);
        Preferences actual = mapper.readValue(jsonValue, Preferences.class);

        assertThat("same entry", actual, is(expected));
    }

    @Test
    public void testHashCode() {
        Preferences p1 = new Preferences();
        p1.addProperty("1", 1);
        p1.addProperty("true", true);
        p1.addUrlPattern("/test");
        p1.addUrlPattern("/test2");
        Preferences p2 = new Preferences();
        Preferences p3 = new Preferences();
        p3.addProperty("1", 1);
        p3.addProperty("true", true);
        p3.addUrlPattern("/test");
        p3.addUrlPattern("/test2");

        Set<Preferences> set = new HashSet<Preferences>();
        set.add(p1);
        set.add(p2);
        set.add(p3);

        // should only be 2 since e3 and e1 is same
        assertThat(set.size(), is(2));
    }
}
