package org.piraso.server.service;

import org.apache.commons.lang.ArrayUtils;
import org.piraso.api.Preferences;

import java.util.regex.Pattern;

public class BridgeLogger {

    private User user;

    private Preferences preferences;

    private String watchedAddr;

    private Long globalId;

    public BridgeLogger() {
    }

    public BridgeLogger(ResponseLoggerService service) {
        this.user = service.getUser();
        this.preferences = service.getPreferences();
        this.watchedAddr = service.getWatchedAddr();
        this.globalId = service.getGlobalId();
    }

    public boolean isWatched(String remoteAddr) {
        Pattern watchedAddrPattern = Pattern.compile(watchedAddr);
        String[] alternativeWatchedAddrs = AlternativeWatchedAddressProviderManager.INSTANCE.getAlternatives(watchedAddr);

        if(watchedAddr.equals(remoteAddr)) {
            return true;
        }

        if(watchedAddrPattern.matcher(remoteAddr).matches()) {
            return true;
        }

        if(!ArrayUtils.isEmpty(alternativeWatchedAddrs)) {
            for(String addr : alternativeWatchedAddrs) {
                if(addr.equals(remoteAddr)) {
                    return true;
                }
            }
        }

        return false;
    }

    public Long getGlobalId() {
        return globalId;
    }

    public void setGlobalId(Long globalId) {
        this.globalId = globalId;
    }

    public String getWatchedAddr() {
        return watchedAddr;
    }

    public void setWatchedAddr(String watchedAddr) {
        this.watchedAddr = watchedAddr;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }
}
