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
}
