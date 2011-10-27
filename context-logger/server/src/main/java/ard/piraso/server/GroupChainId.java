package ard.piraso.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * concatenated id
 */
public class GroupChainId {
    private static final Pattern REGEX = Pattern.compile("[a-z\\-_]+(\\d)", Pattern.CASE_INSENSITIVE);

    private LinkedList<String> groupIds;

    private Map<Class, Object> properties = new HashMap<Class, Object>(3);

    public GroupChainId(String id) {
        groupIds = new LinkedList<String>();
        groupIds.add(id);
    }

    public GroupChainId(String id, int hashCode) {
        groupIds = new LinkedList<String>();
        groupIds.add(id + "-" + Integer.toHexString(hashCode));
    }

    private GroupChainId(String id, LinkedList<String> groupIds) {
        this.groupIds = new LinkedList<String>(groupIds);
        this.groupIds.add(id);
    }

    public GroupChainId create() {
        StringBuilder buf = new StringBuilder();

        String str = groupIds.getLast();
        Matcher matcher = REGEX.matcher(str);

        if(matcher.find()) {
            int start = matcher.start(1);
            int end = matcher.end(1);
            int count = Integer.parseInt(str.substring(start, end));

            buf.append(str.substring(0, start));
            buf.append(String.valueOf(count + 1));
        } else {
            buf.append(str).append("_2");
        }

        return create(buf.toString());
    }

    public void addProperty(Class clazz, Object value) {
        properties.put(clazz, value);
    }

    public Object getProperty(Class clazz){
        return properties.get(clazz);
    }

    public boolean hasProperty(Class clazz) {
        return properties.containsKey(clazz);
    }

    public GroupChainId create(int id) {
        GroupChainId logId = new GroupChainId(String.valueOf(id), groupIds);
        logId.properties.putAll(properties);

        return logId;
    }

    public GroupChainId create(String id, int hashCode) {
        GroupChainId logId = new GroupChainId(id + Integer.toHexString(hashCode), groupIds);
        logId.properties.putAll(properties);

        return logId;
    }

    public GroupChainId create(String id) {
        GroupChainId logId = new GroupChainId(id, groupIds);
        logId.properties.putAll(properties);

        return logId;
    }

    public LinkedList<String> getGroupIds() {
        return groupIds;
    }

    @Override
    public String toString() {
        return groupIds.toString();
    }
}
