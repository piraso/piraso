package ard.piraso.client.net;

import org.apache.commons.lang.Validate;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * Base class for authentication.
 */
public abstract class AbstractHttpAuthentication extends AbstractHttpHandler {

    protected String userName;

    protected String password;

    public AbstractHttpAuthentication(AbstractHttpClient client, HttpContext context) {
        super(client, context);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    protected void validate() {
        Validate.notNull(userName, "userName should not be null.");
        Validate.notNull(password, "password should not be null.");
        Validate.notNull(targetHost, "targetHost should not be null.");
    }

    @Override
    public void execute() throws IOException {
        validate();
        authenticate();
    }

    public abstract void authenticate();
}
