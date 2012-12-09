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

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.List;

/**
 * Spring xml modifier for piraso.
 */
@Mojo(name="spring-xml", threadSafe = true)
public class SpringXmlPirasoModifier extends AbstractXMLPirasoModifier {

    /**
     * the spring.xml file to modify
     */
    @Parameter(required = true)
    private File springXml;

    /**
     * output directed
     */
    @Parameter(required = true)
    private File outputDirectory;

    /**
     * Class replacements
     */
    @Parameter(required = true)
    private List<ClassReplacement> classReplacements;

    public void setClassReplacements(List<ClassReplacement> classReplacements) {
        this.classReplacements = classReplacements;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setSpringXml(File springXml) {
        this.springXml = springXml;
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        if(springXml == null || !springXml.isFile()) {
            throw new MojoExecutionException("'springXml' configuration is expected and should be a valid file.");
        }
        if(outputDirectory == null) {
            throw new MojoExecutionException("'outputDirectory' configuration is expected.");
        }
        if(classReplacements == null || classReplacements.isEmpty()) {
            throw new MojoExecutionException("'classReplacements' configuration is expected and should not be empty.");
        }

        parseDocument(springXml);

        try {
            for(ClassReplacement replacement : classReplacements) {
                NodeList beanList = document.getElementsByTagName("bean");

                for(int i = 0; i < beanList.getLength(); i++) {
                    Element el = (Element) beanList.item(i);

                    if(StringUtils.equals(replacement.getClassName(), el.getAttribute("class"))) {
                        el.setAttribute("class", replacement.getReplacementClassName());
                        if(el.getAttribute("id") != null) {
                            getLog().info(String.format("Replacing class '%s' for bean '%s' with '%s'", el.getAttribute("id"), replacement.getClassName(), replacement.getReplacementClassName()));
                        } else {
                            getLog().info(String.format("Replacing class '%s' with '%s'", replacement.getClassName(), replacement.getReplacementClassName()));
                        }
                    }
                }
            }

            writeDocument(outputDirectory, springXml.getName());
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
