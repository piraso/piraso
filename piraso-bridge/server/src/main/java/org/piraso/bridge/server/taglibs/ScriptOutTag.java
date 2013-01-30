package org.piraso.bridge.server.taglibs;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Start streaming all contained scripts to response output stream.
 *
 */
public class ScriptOutTag extends TagSupport {

    /**
     * {@inheritDoc}
     */
    @Override
    public int doStartTag() throws JspException {
        try {
            ScriptContainer.get(pageContext).write(pageContext.getOut());

            return Tag.SKIP_BODY;
        } catch (IOException e) {
            throw new JspException("Unable to write scripts to jsp writer.");
        }
    }
}

