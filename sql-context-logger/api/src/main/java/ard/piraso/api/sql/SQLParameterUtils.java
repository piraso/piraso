package ard.piraso.api.sql;

import ard.piraso.api.entry.ObjectEntry;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Helper for generating parameter values.
 */
public class SQLParameterUtils {

    private static final List<String> LITERAL_TYPES = Arrays.asList(
            Boolean.class.getName(),
            Boolean.TYPE.getName(),
            Byte.class.getName(),
            Byte.TYPE.getName(),
            Double.class.getName(),
            Float.class.getName(),
            Integer.class.getName(),
            Integer.TYPE.getName(),
            Long.class.getName(),
            Long.TYPE.getName(),
            Short.class.getName(),
            Short.TYPE.getName(),
            BigDecimal.class.getName(),
            BigInteger.class.getName()
    );

    public static String toPSLiteral(SQLParameterEntry parameter) {
        if(CollectionUtils.size(parameter.getParameterClassNames()) < 1) {
            return "''";
        }

        String parameterClassName = parameter.getParameterClassNames()[1];
        ObjectEntry parameterValue = parameter.getArguments()[1];

        if(parameterValue.isNull()) {
            return "is null";
        }

        if(LITERAL_TYPES.contains(parameterClassName)) {
            return parameterValue.getStrValue();
        } else if(Date.class.getName().equals(parameterClassName)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

            return "'" + dateFormat.format(parameterValue.toObject()) + "'";
        } else if(Timestamp.class.getName().equals(parameterClassName)) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

            return "'" + timeFormat.format(parameterValue.toObject()) + "'";
        } else if(Time.class.getName().equals(parameterClassName)) {
            return "'" + parameterValue.toObject().toString() + "'";
        } else {
            return "'" + parameterValue.getStrValue() + "'";
        }
    }

    public static String toRSString(SQLParameterEntry parameter) {
        ObjectEntry returnedValue = parameter.getReturnedValue();
        if(returnedValue == null || returnedValue.isNull()) {
            return "";
        }

        if(LITERAL_TYPES.contains(parameter.getReturnClassName())) {
            return returnedValue.getStrValue();
        } else if(Date.class.getName().equals(parameter.getReturnClassName())) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

            return dateFormat.format(returnedValue.toObject());
        } else if(Timestamp.class.getName().equals(parameter.getReturnClassName())) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

            return timeFormat.format(returnedValue.toObject());
        } else if(Time.class.getName().equals(parameter.getReturnClassName())) {
            return returnedValue.toObject().toString();
        }

        return returnedValue.getStrValue();
    }
}
