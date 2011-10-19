package ard.piraso.api.sql;

import ard.piraso.api.entry.ElapseTimeAware;
import ard.piraso.api.entry.ElapseTimeEntry;
import ard.piraso.api.entry.Entry;

import java.util.HashMap;
import java.util.Map;

/**
 * SQL View entry
 */
public class SQLViewEntry implements Entry, ElapseTimeAware {

    private ElapseTimeEntry elapseTime;

    private Map<Integer, SQLParameterEntry> parameters;

    private String sql;

    public SQLViewEntry() {}

    public SQLViewEntry(String sql, Map<Integer, SQLParameterEntry> parameters, ElapseTimeEntry elapseTime) {
        this.sql = sql;
        this.parameters = new HashMap<Integer, SQLParameterEntry>(parameters);
        this.elapseTime = elapseTime;
    }

    public ElapseTimeEntry getElapseTime() {
        return elapseTime;
    }

    public void setElapseTime(ElapseTimeEntry elapseTime) {
        this.elapseTime = elapseTime;
    }

    public Map<Integer, SQLParameterEntry> getParameters() {
        return parameters;
    }

    public void setParameters(Map<Integer, SQLParameterEntry> parameters) {
        this.parameters = parameters;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
