package org.piraso.server;

public class CustomEntryPoint implements PirasoEntryPoint {

    private String path;

    private String remoteAddr;

    public CustomEntryPoint(String path, String remoteAddr) {
        this.path = path;
        this.remoteAddr = remoteAddr;
    }

    public String getPath() {
        return path;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }
}
