package ard.piraso.api;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;

/**
 * Base test class for testing jackson object mapper for beans.
 */
public abstract class AbstractJacksonTest {
    protected ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
    }
}
