package ard.piraso.server.service;

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!activityUuid.equals(user.activityUuid)) return false;
        if (!remoteAddr.equals(user.remoteAddr)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = remoteAddr.hashCode();
        result = 31 * result + activityUuid.hashCode();
        return result;
    }
}
