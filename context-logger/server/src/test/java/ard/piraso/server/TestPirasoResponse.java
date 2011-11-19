package ard.piraso.server;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Test piraso response.
 */
public class TestPirasoResponse implements PirasoResponse{
    private HttpServletResponse response;

    public TestPirasoResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void setContentType(String contentType) {
        response.setContentType(contentType);
    }

    public void setCharacterEncoding(String encoding) {
        response.setCharacterEncoding(encoding);
    }

    public PrintWriter getWriter() throws IOException {
        return response.getWriter();
    }
}
