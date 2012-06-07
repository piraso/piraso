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

package ard.piraso.maven;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * base class for xml piraso modifier
 */
public abstract class AbstractXMLPirasoModifier extends AbstractMojo {

    protected Document document;

    protected void writeDocument(File outputDirectory, String fileName) throws TransformerException, FileNotFoundException {
        File output = new File(outputDirectory, fileName);

        if(!outputDirectory.isDirectory()) {
            outputDirectory.mkdirs();
        }

        getLog().info("Piraso output xml file: " + output.toString());

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        StreamResult outputTarget = new StreamResult(new FileOutputStream(output));
        DOMSource source = new DOMSource(document);
        transformer.transform(source, outputTarget);
    }


    protected void parseDocument(File file) throws MojoExecutionException {
        try {
            getLog().info("Piraso input xml file: " + file.toString());

            DOMParser parser = new DOMParser();
            parser.parse(new InputSource(new FileReader(file)));

            document = parser.getDocument();
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
