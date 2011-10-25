package ard.piraso.server.service;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Represents a piraso user. This user monitors context logs.
 */
public class User {
    public String remoteAddr;

    public String activityUuid;

    public User(HttpServletRequest request) {
        remoteAddr = request.getRemoteAddr();

        if(request.getParameter("activity_uuid") != null) {
            activityUuid = request.getParameter("activity_uuid");
        } else {
            activityUuid = UUID.randomUUID().toString();
        }
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public String getActivityUuid() {
        return activityUuid;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
