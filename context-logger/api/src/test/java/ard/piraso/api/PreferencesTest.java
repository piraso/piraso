package ard.piraso.api;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
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

        assertThat(preferences.isEnabled("true"), is(true));
        assertThat(preferences.isEnabled("not existing"), is(false));
        assertThat(preferences.getIntValue("1"), is(1));
        assertThat(preferences.getIntValue("not existsing"), CoreMatchers.<Object>nullValue());
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
}
