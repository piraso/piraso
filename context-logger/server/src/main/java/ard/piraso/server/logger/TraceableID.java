package ard.piraso.server.logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * concatenated id
 */
public class TraceableID {
    private static final Pattern REGEX = Pattern.compile("[a-z\\-_]+(\\d)", Pattern.CASE_INSENSITIVE);

    private LinkedList<String> ids;

    private Map<Class, Object> properties = new HashMap<Class, Object>(3);

    public TraceableID(String id) {
        ids = new LinkedList<String>();
        ids.add(id);
    }

    private TraceableID(String id, LinkedList<String> ids) {
        this.ids = new LinkedList<String>(ids);
        this.ids.add(id);
    }

    public TraceableID create() {
        StringBuilder buf = new StringBuilder();

        String str = ids.getLast();
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

    public TraceableID create(int id) {
        TraceableID logId = new TraceableID(String.valueOf(id), ids);
        logId.properties.putAll(properties);

        return logId;
    }

    public TraceableID create(String id, int hashCode) {
        TraceableID logId = new TraceableID(id + Integer.toHexString(hashCode), ids);
        logId.properties.putAll(properties);

        return logId;
    }

    public TraceableID create(String id) {
        TraceableID logId = new TraceableID(id, ids);
        logId.properties.putAll(properties);

        return logId;
    }

    public LinkedList<String> getIds() {
        return ids;
    }

    @Override
    public String toString() {
        return ids.toString();
    }
}
