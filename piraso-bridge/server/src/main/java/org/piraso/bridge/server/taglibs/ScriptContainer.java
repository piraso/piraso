package org.piraso.bridge.server.taglibs;

import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * This will serve as a helper for {@link ScriptInTag} and {@link ScriptOutTag}. This holds all pushed scripts.
 *
 * @version $Id: $
 */
public class ScriptContainer {

    /**
     * Name of the attribute name where the javascripts are placed.
     */
    static final String ATTR_NAME = ScriptContainer.class.getName();

    /**
     * Creates or retrieves the {@link ScriptContainer} instance.
     *
     * @param pageContext the pageContext
     * @return the instance
     */
    public static ScriptContainer get(PageContext pageContext) {
        Object attribute = pageContext.getRequest().getAttribute(ATTR_NAME);

        if(attribute != null && !ScriptContainer.class.isAssignableFrom(attribute.getClass())) {
            throw new IllegalArgumentException(ATTR_NAME + " has already been set to a(n) " + attribute.getClass());
        }

        ScriptContainer scriptContainer;

        if(attribute == null) {
            scriptContainer = new ScriptContainer();

            pageContext.getRequest().setAttribute(ATTR_NAME, scriptContainer);
        } else {
            scriptContainer = (ScriptContainer) attribute;
        }

        return scriptContainer;
    }

    /**
     * script content body are contained from this list
     */
    protected List<String> scripts = new LinkedList<String>();

    /**
     * Prevent other class from constructing instance of this class.
     */
    private ScriptContainer() {}

    /**
     * Adds the body content to the scripts
     *
     * @param bodyContent the script body content
     */
    public void push(BodyContent bodyContent) {
        scripts.add(bodyContent.getString());
    }

    /**
     * Writes the {@link #scripts} to the <code>jspWriter</code>.
     *
     * @param jspWriter the response output writer
     * @throws java.io.IOException on unexpected io error
     */
    public void write(JspWriter jspWriter) throws IOException {
        jspWriter.write(StringUtils.join(scripts, "\n"));
    }
}

