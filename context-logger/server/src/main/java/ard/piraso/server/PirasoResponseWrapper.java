package ard.piraso.server;

import ard.piraso.api.entry.ResponseEntry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * Responsible to collect response information.
 */
public class PirasoResponseWrapper extends HttpServletResponseWrapper {

    private ResponseEntry entry;

    public PirasoResponseWrapper(HttpServletResponse response, ResponseEntry entry) {
        super(response);

        this.entry = entry;
    }

    @Override
    public void addCookie(Cookie cookie) {
        super.addCookie(cookie);
        entry.addCookie(WebEntryUtils.toEntry(cookie));
    }

    @Override
    public void addDateHeader(String name, long date) {
        super.addDateHeader(name, date);
        entry.addHeader(name, date);
    }

    @Override
    public void addHeader(String name, String value) {
        super.addHeader(name, value);
        entry.addHeader(name, value);
    }

    @Override
    public void addIntHeader(String name, int value) {
        super.addIntHeader(name, value);
        entry.addHeader(name, value);
    }

    @Override
    public void sendError(int sc) throws IOException {
        super.sendError(sc);
        entry.setStatus(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        super.sendError(sc, msg);

        entry.setStatus(sc);
        entry.setStatusReason(msg);
    }

    @Override
    public void setDateHeader(String name, long date) {
        super.setDateHeader(name, date);
        entry.addHeader(name, date);
    }

    @Override
    public void setHeader(String name, String value) {
        super.setHeader(name, value);
        entry.addHeader(name, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
        super.setIntHeader(name, value);
        entry.addHeader(name, value);
    }

    @Override
    public void setStatus(int sc) {
        super.setStatus(sc);
        entry.setStatus(sc);
    }

    @Override
    public void setStatus(int sc, String sm) {
        super.setStatus(sc, sm);

        entry.setStatus(sc);
        entry.setStatusReason(sm);
    }
}
