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

package org.piraso.io.impl;

import org.piraso.api.io.PirasoEntryWriter;
import org.piraso.io.IOEntry;
import org.piraso.io.IOEntryManager;
import org.piraso.io.IOEntryReader;
import org.piraso.io.IOEntryVisitor;
import org.apache.commons.io.IOUtils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author adleon
 */
public class FileEntrySave implements IOEntryVisitor {
    
    private static final Logger LOG = Logger.getLogger(FileEntrySave.class.getName());
    
    private IOEntryManager manager;
    
    private IOEntryReader reader;
    
    private List<Long> requests;
    
    private PirasoEntryWriter writer;
    
    public FileEntrySave(IOEntryReader reader) {
        this.reader = reader;
        this.manager = reader.getManager();
        this.requests = new ArrayList<Long>();
    }
    
    public void addRequest(Long id) {
        requests.add(id);
    }
    
    public void save(File file) throws IOException, ParserConfigurationException, TransformerConfigurationException {
        FileOutputStream fo = null;
        GZIPOutputStream go = null;
        OutputStreamWriter io = null;
        PrintWriter pw = null;
        
        try {
             fo = new FileOutputStream(file);
             go = new GZIPOutputStream(fo);
             io = new OutputStreamWriter(go);
             pw = new PrintWriter(io, true);
             
             writer = new PirasoEntryWriter(reader.getId(), reader.getWatchedAddr(), pw);
             
             manager.visit(requests, this);
        } finally {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(pw);
            IOUtils.closeQuietly(io);
            IOUtils.closeQuietly(go);
            IOUtils.closeQuietly(fo);
        }
    }

    public void visit(IOEntry entry) {
        try {
            writer.write(entry.getDate(), entry.getEntry());
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }
}
