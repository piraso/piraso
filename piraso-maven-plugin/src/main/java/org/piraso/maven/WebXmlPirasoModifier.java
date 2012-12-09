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

package org.piraso.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

/**
 * Web xml Piraso modifier.
 *
 */
@Mojo(name="web-xml", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class WebXmlPirasoModifier extends AbstractXMLPirasoModifier {

    /**
     * The Maven project.
     */
    @Component
    protected MavenProject project;

    /**
     * piraso logging path
     */
    @Parameter( defaultValue = "/piraso/logging", required = true )
    private String pirasoLoggingPath = "/piraso/logging";

    /**
     * the web.xml file to modify
     */
    @Parameter( defaultValue = "${project.basedir}/src/main/webapp/WEB-INF/web.xml", required = true )
    private File webXml;

    /**
     * output directed
     */
    @Parameter( defaultValue = "${project.build.directory}/piraso/WEB-INF/", required = true)
    private File outputDirectory;

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setPirasoLoggingPath(String pirasoLoggingPath) {
        this.pirasoLoggingPath = pirasoLoggingPath;
    }

    public void setWebXml(File webXml) {
        this.webXml = webXml;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    /**
     * Start modifying the web.xml
     * @throws MojoExecutionException on error
     * @throws MojoFailureException on error
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        if(webXml == null || !webXml.isFile()) {
            throw new MojoExecutionException("'webXml' configuration is expected and should be a valid file.");
        }
        if(outputDirectory == null) {
            throw new MojoExecutionException("'outputDirectory' configuration is expected.");
        }

        parseDocument(webXml);

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
        insertContextParam(root, firstContextParam, "contextClass", "org.piraso.server.spring.web.PirasoWebApplicationContext");
        insertFilterAndServletElement(root, firstFilterOrServlet);

        try {
            File outputFile = new File(outputDirectory, webXml.getName());
            writeDocument(outputFile);
            project.getProperties().setProperty("maven.war.webxml", outputFile.getAbsolutePath());
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
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
        Element servlet = createElementNameValue("servlet", "servlet-name", "servlet-class", "pirasoServlet", "org.springframework.web.context.support.HttpRequestHandlerServlet", 0);
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
        return createElementNameValue(node, nameNode, valueNode, name, value, -1);
    }

    private Element createElementNameValue(String node, String nameNode, String valueNode, String name, String value, int loadOnStartup) {
        Element mainEl = document.createElement(node);
        Element paramName = document.createElement(nameNode);
        Element paramValue = document.createElement(valueNode);
        Element loadOnStartUpEl = document.createElement("load-on-startup");


        paramName.setTextContent(name);
        paramValue.setTextContent(value);

        if(loadOnStartup >= 0) {
            loadOnStartUpEl.setTextContent(String.valueOf(loadOnStartup));
        }

        mainEl.appendChild(document.createTextNode("\n    "));
        mainEl.appendChild(paramName);
        mainEl.appendChild(document.createTextNode("\n    "));
        mainEl.appendChild(paramValue);
        mainEl.appendChild(document.createTextNode("\n  "));

        if(loadOnStartup >= 0) {
            mainEl.appendChild(document.createTextNode("  "));
            mainEl.appendChild(loadOnStartUpEl);
            mainEl.appendChild(document.createTextNode("\n  "));
        }

        return mainEl;
    }
}
