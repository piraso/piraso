package ard.piraso.api.entry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a request entry
 */
public class RequestEntry extends Entry {

    private String uri;

    private String queryString;

    private String method;

    private String remoteAddr;

    private Map<String, String> headers;

    private Map<String, String[]> parameters;

    private List<CookieEntry> cookies;

    public RequestEntry() {
    }

    public RequestEntry(String uri) {
        this.uri = uri;
    }

    public void addHeader(String name, String value) {
        if(headers == null) {
            headers = new LinkedHashMap<String, String>();
        }

        headers.put(name, value);
    }

    public void addCookie(CookieEntry cookieEntry) {
        if(cookies == null) {
            cookies = new ArrayList<CookieEntry>();
        }

        cookies.add(cookieEntry);
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(LinkedHashMap<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String[]> getParameters() {
        return parameters;
    }

    public void setParameters(LinkedHashMap<String, String[]> parameters) {
        this.parameters = parameters;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<CookieEntry> getCookies() {
        return cookies;
    }

    public void setCookies(List<CookieEntry> cookies) {
        this.cookies = cookies;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }
}
