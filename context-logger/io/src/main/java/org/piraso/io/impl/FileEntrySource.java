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

import org.piraso.api.io.EntryReadListener;
import org.piraso.api.io.PirasoEntryReader;
import org.piraso.io.IOEntrySource;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author adleon
 */
public class FileEntrySource implements IOEntrySource {
    private static final Logger LOG = Logger.getLogger(FileEntrySource.class.getName());
        
    private PirasoEntryReader reader;
    
    private boolean alive;

    private File source;

    private String name;

    public FileEntrySource(File source) throws FileNotFoundException {
        Validate.notNull(source, "source should not be null.");
        this.source = source;

        if(!source.isFile()) {
            throw new FileNotFoundException(String.format("File %s not found.", source.getAbsolutePath()));
        }
        
        this.name = source.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void reset() {
        try {
            alive = false;
            reader = new PirasoEntryReader(new GZIPInputStream(new FileInputStream(source)));
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public IOEntrySource createNew() {
        throw new UnsupportedOperationException();
    }

    public String getName() {
        return name;
    }

    public void start() {
        try {
            if(!alive) {
                LOG.info("Starting Context Monitor for File : " + source.getAbsolutePath());
                alive = true;
                reader.start();
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            LOG.info("Stopped Context Monitor for URL : " + source.getAbsolutePath());
            alive = false;
        }
    }

    public void stop() {
        try {
            if(alive) {
                LOG.info("Starting Context Monitor for File : " + source.getAbsolutePath());
                reader.stop();
            } else {
                LOG.warning("Not stopped since not alive. File: " + source.getAbsolutePath());
            }
        } finally {
            alive = false;
        }
    }

    public String getId() {
        return reader.getId();
    }

    public boolean isAlive() {
        return alive;
    }

    public void addListener(EntryReadListener listener) {
        reader.addListener(listener);
    }

    public void removeListener(EntryReadListener listener) {
        reader.removeListener(listener);
    }

    public boolean isRestartable() {
        return false;
    }

    public String getWatchedAddr() {
        if(reader == null) {
            return null;
        }

        return reader.getWatchedAddr();
    }

}
