package ard.piraso.client.net;

import java.io.IOException;

/**
 * Http piraso exception.
 */
public class HttpPirasoException extends IOException {
    public HttpPirasoException(String s) {
        super(s);
    }

    public HttpPirasoException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
