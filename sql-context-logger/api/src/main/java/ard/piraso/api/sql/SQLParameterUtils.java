/*
 * Copyright (c) 2012. Piraso Alvin R. de Leon. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The Piraso licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ard.piraso.api.sql;

import ard.piraso.api.entry.ObjectEntry;
import ard.piraso.api.entry.ObjectEntryUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

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

        if(!parameterValue.isSupported()) {
            return "@not-supported";
        }

        if(parameterValue.isNull()) {
            return "is null";
        }

        if("setNull".equals(parameter.getMethodName())) {
            return "null";
        }

        if(LITERAL_TYPES.contains(parameterClassName)) {
            return parameterValue.getStrValue();
        } else if(Date.class.getName().equals(parameterClassName)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

            return "'" + dateFormat.format(parameterValue.toObject()) + "'";
        } else if(Timestamp.class.getName().equals(parameterClassName)) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

            return "'" + timeFormat.format(parameterValue.toObject()) + "'";
        } else {
            if(parameterValue.isSupported()) {
                return "'" + String.valueOf(parameterValue.toObject()) + "'";
            }

            return "'" + parameterValue.getStrValue() + "'";
        }
    }

    public static String toRSString(SQLParameterEntry parameter) {
        ObjectEntry returnedValue = parameter.getReturnedValue();
        if(returnedValue == null || returnedValue.isNull()) {
            return "@null";
        }

        if(!returnedValue.isSupported()) {
            return "@not-supported";
        }

        if(LITERAL_TYPES.contains(parameter.getReturnClassName())) {
            return returnedValue.getStrValue();
        } else if(Date.class.getName().equals(parameter.getReturnClassName())) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

            return dateFormat.format(returnedValue.toObject());
        } else if(Timestamp.class.getName().equals(parameter.getReturnClassName())) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

            return timeFormat.format(returnedValue.toObject());
        }

        if(returnedValue.isSupported()) {
            return String.valueOf(returnedValue.toObject());
        }

        return returnedValue.getStrValue();
    }

    public static Vector<Vector<String>> createColumnDefinition(SQLDataViewEntry entry) {
        if(CollectionUtils.isEmpty(entry.getRecords())) {
            return new Vector<Vector<String>>();
        }

        List<SQLParameterEntry> firstRow = entry.getRecords().iterator().next();
        Vector<Vector<String>> data = new Vector<Vector<String>>();

        for (SQLParameterEntry param : firstRow) {
            Vector<String> v = new Vector<String>(2);
            v.add(ObjectEntryUtils.toString(param.getArguments()[0]));
            v.add(param.getReturnClassName());

            data.add(v);
        }

        return data;
    }

    public static Vector<String> createHeaders(SQLDataViewEntry entry, int maxColumnToleranceSize) {
        if(CollectionUtils.isEmpty(entry.getRecords())) {
            return new Vector<String>();
        }

        List<SQLParameterEntry> firstRow = entry.getRecords().iterator().next();
        Vector<String> header = new Vector<String>();

        if(firstRow.size() > maxColumnToleranceSize) {
            header.add("Column Name/ID");
            header.add("Value");
        } else {
            for(SQLParameterEntry param : firstRow) {
                header.add(ObjectEntryUtils.toString(param.getArguments()[0]));
            }
        }

        return header;
    }

    public static Vector<Vector<String>> createDataValues(SQLDataViewEntry entry, int maxColumnToleranceSize) {
        if(CollectionUtils.isEmpty(entry.getRecords())) {
            return new Vector<Vector<String>>();
        }

        List<SQLParameterEntry> firstRow = entry.getRecords().iterator().next();
        Vector<Vector<String>> data = new Vector<Vector<String>>();

        if(firstRow.size() > maxColumnToleranceSize) {
            int j = 1;
            for (List<SQLParameterEntry> row : entry.getRecords()) {
                Vector<String> v = new Vector<String>();
                v.add("@Row " + (j++));
                v.add(StringUtils.repeat("-", 100));
                data.add(v);
                for (SQLParameterEntry param : row) {
                    v = new Vector<String>();
                    v.add(ObjectEntryUtils.toString(param.getArguments()[0]));
                    v.add(ObjectEntryUtils.toString(param.getReturnedValue()));
                    data.add(v);
                }
            }
        } else {
            for (List<SQLParameterEntry> row : entry.getRecords()) {
                Vector<String> v = new Vector<String>(row.size());
                for (SQLParameterEntry aRow : row) {
                    v.add(ObjectEntryUtils.toString(aRow.getReturnedValue()));
                }
                data.add(v);
            }
        }

        return data;
    }
}
