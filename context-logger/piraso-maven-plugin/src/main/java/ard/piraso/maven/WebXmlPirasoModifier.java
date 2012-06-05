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
import org.apache.maven.plugin.MojoFailureException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
 * Web xml Piraso modifier.
 *
 * @goal web-xml
 */
public class WebXmlPirasoModifier  extends AbstractMojo {

    /**
     * piraso logging path
     *
     * @parameter default-value="/piraso/logging"
     */
    private String pirasoLoggingPath = "/piraso/logging";

    /**
     * the web.xml file to modify
     *
     * @parameter default-value="${maven.war.webxml}"
     */
    private File webXml;

    /**
     * output directed
     *
     * @parameter default-value="${project.build.directory}/piraso/WEB-INF/"
     */
    private File outputDirectory;

    private Document document;


    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setPirasoLoggingPath(String pirasoLoggingPath) {
        this.pirasoLoggingPath = pirasoLoggingPath;
    }

    public void setWebXml(File webXml) {
        this.webXml = webXml;
    }

    /**
     * Start modifying the web.xml
     * @throws MojoExecutionException on error
     * @throws MojoFailureException on error
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        parseDocument();

        Node root = document.getElementsByTagName("web-app").item(0);
        Node firstContextParam = document.getElementsByTagName("context-param").item(0);
        NodeList firstFilters = document.getElementsByTagName("filter");

        Node firstFilterOrServlet;
        if(firstFilters != null && firstFilters.getLength() > 0) {
            firstFilterOrServlet = firstFilters.item(0);
        } else {
            firstFilterOrServlet = document.getElementsByTagName("servlet").item(0);
        }

        insertContextParam(root, firstContextParam, "parentContextKey", "piraso.context");
        insertContextParam(root, firstContextParam, "contextClass", "ard.piraso.server.spring.web.PirasoWebApplicationContext");
        insertFilterAndServletElement(root, firstFilterOrServlet);

        try {
            writeDocument();
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private void writeDocument() throws TransformerException, FileNotFoundException {
        File output = new File(outputDirectory, "web.xml");

        if(!output.getParentFile().isDirectory()) {
            output.getParentFile().mkdirs();
        }

        getLog().info("Piraso output web xml file: " + output.toString());

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        StreamResult outputTarget = new StreamResult(new FileOutputStream(output));
        DOMSource source = new DOMSource(document);
        transformer.transform(source, outputTarget);
    }

    private void insertContextParam(Node root, Node insertBefore, String name, String value) {
        Node buf = document.createTextNode("\n  ");
        Node contextParam = createContextParam(name, value);
        root.insertBefore(buf, insertBefore);
        root.insertBefore(contextParam, buf);
    }

    private void insertFilterAndServletElement(Node root, Node insertBefore) {
        Element filter = createElementNameValue("filter", "filter-name", "filter-class", "pirasoFilter", "org.springframework.web.filter.DelegatingFilterProxy");
        Element filterMapping = createElementNameValue("filter-mapping", "filter-name", "url-pattern", "pirasoFilter", "/*");
        Element servlet = createElementNameValue("servlet", "servlet-name", "servlet-class", "pirasoServlet", "org.springframework.web.context.support.HttpRequestHandlerServlet");
        Element servletMapping = createElementNameValue("servlet-mapping", "servlet-name", "url-pattern", "pirasoServlet", pirasoLoggingPath);

        Node buf1 = document.createTextNode("\n  ");
        Node buf2 = document.createTextNode("\n  ");
        Node buf3 = document.createTextNode("\n  ");
        Node buf4 = document.createTextNode("\n  ");

        root.insertBefore(buf1, insertBefore);
        root.insertBefore(servletMapping, buf1);

        root.insertBefore(buf2, servletMapping);
        root.insertBefore(servlet, buf2);

        root.insertBefore(buf3, servlet);
        root.insertBefore(filterMapping, buf3);

        root.insertBefore(buf4, filterMapping);
        root.insertBefore(filter, buf4);
    }


    private Element createContextParam(String name, String value) {
        return createElementNameValue("context-param", "param-name", "param-value", name, value);
    }

    private Element createElementNameValue(String node, String nameNode, String valueNode, String name, String value) {
        Element filter = document.createElement(node);
        Element paramName = document.createElement(nameNode);
        Element paramClass = document.createElement(valueNode);

        paramName.setTextContent(name);
        paramClass.setTextContent(value);

        filter.appendChild(document.createTextNode("\n    "));
        filter.appendChild(paramName);
        filter.appendChild(document.createTextNode("\n    "));
        filter.appendChild(paramClass);
        filter.appendChild(document.createTextNode("\n  "));

        return filter;
    }

    private void parseDocument() throws MojoExecutionException {
        try {
            getLog().info("Piraso input web xml file: " + webXml.toString());

            DOMParser parser = new DOMParser();
            parser.parse(new InputSource(new FileReader(webXml)));

            document = parser.getDocument();
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
