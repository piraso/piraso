package ard.piraso.server;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Defines an interface of a response.
 */
public interface PirasoResponse {

    void setContentType(String contentType);

    void setCharacterEncoding(String encoding);

    PrintWriter getWriter() throws IOException;
}
