package ard.piraso.api.sql;

import ard.piraso.api.Level;

/**
 * SQL preferences enum
 */
public enum SQLPreferenceEnum {
    CONNECTION_METHOD_CALL_ENABLED("sql.connection.method.call.enabled"),

    CONNECTION_ENABLED("sql.connection.enabled"),

    PREPARED_STATEMENT_ENABLED("sql.prepared.statement.enabled"),

    PREPARED_STATEMENT_METHOD_CALL_ENABLED("sql.prepared.statement.method.call.enabled"),

    VIEW_SQL_ENABLED("sql.view.enabled"),

    RESULTSET_ENABLED("sql.resultset.enabled"),

    VIEW_DATA_SIZE("sql.data.size", false),

    RESULTSET_METHOD_CALL_ENABLED("sql.resultset.method.call.enabled");

    // register enum as level
    static {
        for(SQLPreferenceEnum flag : SQLPreferenceEnum.values()) {
            if(flag.isLevel()) {
                Level.addLevel(flag.getPropertyName());
            }
        }
    }

    private String propertyName;

    private boolean level;

    private SQLPreferenceEnum(String propertyName) {
        this(propertyName, true);
    }

    private SQLPreferenceEnum(String propertyName, boolean level) {
        this.propertyName = propertyName;
        this.level = level;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public boolean isLevel() {
        return level;
    }
}
