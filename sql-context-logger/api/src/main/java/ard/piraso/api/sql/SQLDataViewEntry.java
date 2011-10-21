package ard.piraso.api.sql;

import ard.piraso.api.entry.Entry;

import java.util.List;

/**
 * represents a resultSet SQL data
 */
public class SQLDataViewEntry implements Entry {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SQLDataViewEntry that = (SQLDataViewEntry) o;

        if (resultSetId != that.resultSetId) return false;
        if (records != null ? !records.equals(that.records) : that.records != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = records != null ? records.hashCode() : 0;
        result = 31 * result + (int) (resultSetId ^ (resultSetId >>> 32));
        return result;
    }
}
