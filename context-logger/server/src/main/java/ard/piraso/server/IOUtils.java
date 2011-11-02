package ard.piraso.server;

import java.io.Closeable;
import java.io.IOException;

/**
 * Contains io utilities
 */
public final class IOUtils {

    private IOUtils() {}

    public static void closeQuitely(Closeable closeable) throws IOException {
        if(closeable != null) {
            closeable.close();
        }
    }
}
