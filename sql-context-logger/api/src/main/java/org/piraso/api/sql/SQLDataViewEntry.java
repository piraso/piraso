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

package org.piraso.api.sql;

import org.piraso.api.entry.Entry;
import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * represents a resultSet SQL data
 */
public class SQLDataViewEntry extends Entry {

    private List<List<SQLParameterEntry>> records;

    private long resultSetId;

    public SQLDataViewEntry() {
    }

    public SQLDataViewEntry(long resultSetId, List<List<SQLParameterEntry>> records) {
        this.records = new ArrayList<List<SQLParameterEntry>>(records);
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

    @JsonIgnore
    public String getCSVString() throws IOException {
        if(CollectionUtils.isEmpty(records)) {
            return "";
        }

        StringWriter writer = new StringWriter();
        CSVWriter csv = new CSVWriter(writer);

        Vector<String> header = SQLParameterUtils.createHeaders(this, Integer.MAX_VALUE);
        csv.writeNext(header.toArray(new String[header.size()]));

        for(List<SQLParameterEntry> list : records) {
            List<String> row = new ArrayList<String>(list.size());
            for(SQLParameterEntry parameter : list) {
                row.add(SQLParameterUtils.toRSString(parameter));
            }

            csv.writeNext(row.toArray(new String[list.size()]));
        }

        csv.flush();
        return writer.toString();
    }
}
