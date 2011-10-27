package ard.piraso.api.entry;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Defines a base entry class.
 */
public class Entry {

    protected Long requestId;

    protected GroupEntry group;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public GroupEntry getGroup() {
        return group;
    }

    public void setGroup(GroupEntry group) {
        this.group = group;
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
