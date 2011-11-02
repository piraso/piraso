package ard.piraso.server;

import ard.piraso.api.entry.CookieEntry;
import ard.piraso.api.entry.RequestEntry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;

/**
 * Coverts web objects to Entry types.
 */
public final class WebEntryUtils {

    private WebEntryUtils() {}

    public static CookieEntry toEntry(Cookie cookie) {
        CookieEntry entry = new CookieEntry();

        entry.setName(cookie.getName());
        entry.setValue(cookie.getValue());
        entry.setComment(cookie.getComment());
        entry.setDomain(cookie.getDomain());
        entry.setMaxAge(cookie.getMaxAge());
        entry.setPath(cookie.getPath());
        entry.setSecure(cookie.getSecure());
        entry.setVersion(cookie.getVersion());

        return entry;
    }

    @SuppressWarnings("unchecked")
    public static RequestEntry toEntry(HttpServletRequest request) {
        RequestEntry entry = new RequestEntry(request.getRequestURI());

        entry.setMethod(request.getMethod());
        entry.setQueryString(request.getQueryString());
        entry.setRemoteAddr(request.getRemoteAddr());
        entry.setParameters(new LinkedHashMap<String, String[]>(request.getParameterMap()));

        Enumeration<String> enumeration = request.getHeaderNames();
        while(enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            entry.addHeader(name, value);
        }

        for(Cookie cookie : request.getCookies()) {
           entry.addCookie(toEntry(cookie));
        }

        return entry;
    }
}
