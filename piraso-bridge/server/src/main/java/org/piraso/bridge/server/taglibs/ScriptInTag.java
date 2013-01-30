package org.piraso.bridge.server.taglibs;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

/**
 * ScriptInTag.
 *
 * @version $Id: $
 */
public class ScriptInTag extends BodyTagSupport {

    /**
     * Puts the body content inside an attribute <code>jsContainer</code>.
     * @throws javax.servlet.jsp.JspException
     */
    public int doAfterBody() throws JspException {
        ScriptContainer container = ScriptContainer.get(pageContext);

        container.push(getBodyContent());

        return Tag.SKIP_BODY;
    }
}

