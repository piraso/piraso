package ard.piraso.api.sql;

import ard.piraso.api.entry.MessageEntry;

/**
 * SQL data total rows entry
 */
public class SQLDataTotalRowsEntry extends MessageEntry {

    private int totalRows;

    public SQLDataTotalRowsEntry() {}

    public SQLDataTotalRowsEntry(int totalRows) {
        super("Total Records Retrieved: " + totalRows);
        this.totalRows = totalRows;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SQLDataTotalRowsEntry that = (SQLDataTotalRowsEntry) o;

        if (totalRows != that.totalRows) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + totalRows;
        return result;
    }
}
