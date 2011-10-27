package ard.piraso.api.entry;

import java.util.List;

/**
 * Group entry
 */
public class GroupEntry extends Entry {
    private List<String> groups;

    public GroupEntry() {}

    public GroupEntry(List<String> groups) {
        this.groups = groups;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
