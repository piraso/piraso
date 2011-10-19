package ard.piraso.api.sql;

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

    VIEW_DATA_SIZE("sql.data.size"),

    RESULTSET_METHOD_CALL_ENABLED("sql.resultset.method.call.enabled");

    private String propertyName;

    private SQLPreferenceEnum(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }

}
