package ard.piraso.server.spring.web;

import ard.piraso.server.PirasoResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Http implementation of {@link PirasoResponse}.
 */
public class PirasoHttpServletResponse implements PirasoResponse {

    private HttpServletResponse response;

    public PirasoHttpServletResponse(HttpServletResponse response) {
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
