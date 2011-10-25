package ard.piraso.api.sql;

import ard.piraso.api.entry.Entry;

import java.util.List;

/**
 * represents a resultSet SQL data
 */
public class SQLDataViewEntry extends Entry {

    private List<List<SQLParameterEntry>> records;

    private long resultSetId;

    public SQLDataViewEntry(long resultSetId, List<List<SQLParameterEntry>> records) {
        this.records = records;
        this.resultSetId = resultSetId;
    }

    public List<List<SQLParameterEntry>> getRecords() {
        return records;
    }

    public void setRecords(List<List<SQLParameterEntry>> records) {
        this.records = records;
    }

    public long getResultSetId() {
        return resultSetId;
    }

    public void setResultSetId(long resultSetId) {
        this.resultSetId = resultSetId;
    }
}
