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

package ard.piraso.io.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author adleon
 */
public final class IOEntryUtils {
    
    private static final Pattern REGEX = Pattern.compile("[a-z0-9\\-_]+(\\d)", Pattern.CASE_INSENSITIVE);
    
    private IOEntryUtils() {}
    
    public static File createRequestDirectory(File baseDir, Long id) {
        return createEntryFolder(baseDir, String.valueOf(id));
    }
    
    public static File createBaseDirectory(String id) {
        File userdir = new File(System.getProperty("user.dir"));
        File vcdir = createEntryFolder(userdir, ".piraso");
        File tmpdir = createEntryFolder(vcdir, ".tmp");
        
        File baseDir = createEntryFolder(tmpdir, createBaseName(tmpdir, id));
        
        baseDir.deleteOnExit();
        
        return baseDir;
    }
    
    private static String createBaseName(File dir, String id) {        
        if(!new File(dir, id).isDirectory()) {
            return id;
        }

        StringBuilder buf = new StringBuilder();
        Matcher matcher = REGEX.matcher(id);

        if(matcher.find()) {
            int start = matcher.start(1);
            int end = matcher.end(1);
            int count = Integer.parseInt(id.substring(start, end));

            buf.append(id.substring(0, start));
            buf.append(String.valueOf(count + 1));
        } else {
            buf.append(id).append("_2");
        }
        
        return createBaseName(dir, buf.toString());
    }
    
    public static File createEntryFile(File parent, Long rowNum) {
        return new File(parent, String.valueOf(rowNum) + ".json");
    }
    
    public static File createEntryFolder(File parent, String name) {
        File dir = new File(parent, name);

        if(!dir.isDirectory()) {
            dir.mkdir();
        }

        return dir;
    }
}
