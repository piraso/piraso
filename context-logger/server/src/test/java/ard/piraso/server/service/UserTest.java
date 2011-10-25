package ard.piraso.server.service;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static ard.piraso.server.CommonMockObjects.createUser;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link User} class.
 */
public class UserTest {

    private static final String EXPECTED_REMOTE_ADDRESS = "127.0.0.1";

    @Test
    public void testCreateUser() throws Exception {
        User user = createUser(EXPECTED_REMOTE_ADDRESS);

        assertEquals(EXPECTED_REMOTE_ADDRESS, user.getRemoteAddr());
        assertNotNull(user.getActivityUuid());
    }

    @Test
    public void testEquals() throws Exception {
        User u1 = createUser(EXPECTED_REMOTE_ADDRESS, "a1");
        User u2 = createUser(EXPECTED_REMOTE_ADDRESS, "a1");

        assertThat(u1.equals(u1), is(true)); // same instance
        assertThat(u1.equals("string"), is(false)); // not same class
        assertThat(u1.equals(null), is(false)); // null compare
        assertThat(u1.equals(u2), is(true)); // same values

        u1 = createUser("r1", "a1");
        u2 = createUser("r1", "a2");

        assertThat(u1.equals(u2), is(false)); // not matched activity uuid

        u1 = createUser("r1", "a1");
        u2 = createUser("r2", "a1");

        assertThat(u1.equals(u2), is(false)); // not matched remote address
    }

    @Test
    public void testHashCode() throws Exception {
        User e1 = createUser(EXPECTED_REMOTE_ADDRESS, "a1");
        User e2 = createUser(EXPECTED_REMOTE_ADDRESS);
        User e3 = createUser(EXPECTED_REMOTE_ADDRESS, "a1");

        Set<User> set = new HashSet<User>();
        set.add(e1);
        set.add(e2);
        set.add(e3);

        // should only be 2 since e3 and e1 is same
        assertThat(set.size(), is(2));
    }
}
