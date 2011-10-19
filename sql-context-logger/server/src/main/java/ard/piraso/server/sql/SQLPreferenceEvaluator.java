package ard.piraso.server.sql;

import ard.piraso.api.sql.SQLPreferenceEnum;
import ard.piraso.server.GeneralPreferenceEvaluator;

/**
 * Evaluates SQL preferences.
 */
public class SQLPreferenceEvaluator extends GeneralPreferenceEvaluator {

    public boolean isConnectionMethodCallEnabled() {
        return isEnabled(SQLPreferenceEnum.CONNECTION_METHOD_CALL_ENABLED);
    }

    public boolean isConnectionEnabled() {
        return isEnabled(SQLPreferenceEnum.CONNECTION_ENABLED);
    }

    public boolean isPreparedStatementMethodCallEnabled() {
        return isEnabled(SQLPreferenceEnum.PREPARED_STATEMENT_METHOD_CALL_ENABLED);
    }

    public boolean isPreparedStatementEnabled() {
        return isEnabled(SQLPreferenceEnum.PREPARED_STATEMENT_ENABLED);
    }

    public boolean isViewSQLEnabled() {
        return isEnabled(SQLPreferenceEnum.VIEW_SQL_ENABLED);
    }

    public boolean isResultSetEnabled() {
        return isEnabled(SQLPreferenceEnum.RESULTSET_ENABLED);
    }

    public boolean isResultSetMethodCallEnabled() {
        return isEnabled(SQLPreferenceEnum.RESULTSET_METHOD_CALL_ENABLED);
    }

    public Integer getMaxDataSize() {
        return getIntValue(SQLPreferenceEnum.VIEW_DATA_SIZE);
    }

    private boolean isEnabled(SQLPreferenceEnum pref) {
        return preference != null && preference.isEnabled(pref.getPropertyName());
    }

    private Integer getIntValue(SQLPreferenceEnum pref) {
        if(preference == null) {
            return null;
        }

        return preference.getIntValue(pref.getPropertyName());
    }
}
