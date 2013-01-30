package org.piraso.bridge.server.taglibs;

import org.springframework.web.servlet.tags.form.AbstractHtmlElementBodyTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

public class ControlGroupTag extends AbstractHtmlElementBodyTag {

    protected String statusId;

    /**
     * Sets the 'varStatus' attribute.
     *
     * @param statusId Name of the exported scoped variable storing the status
     * of the iteration.
     */
    public void setVarStatus(String statusId) {
        this.statusId = statusId;
    }

    @Override
    protected void onWriteTagContent() {
        try {
            pageContext.setAttribute(statusId, new ControlGroupStatus(getBindStatus()));
        } catch (JspException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    protected void renderFromBodyContent(BodyContent bodyContent, TagWriter tagWriter) throws JspException {
        tagWriter.startTag("div");

        if(getBindStatus().getErrors().hasFieldErrors(getBindStatus().getExpression())) {
            tagWriter.writeAttribute("class", getCssErrorClass());
        } else {
            tagWriter.writeAttribute("class", getCssClass());
        }

        tagWriter.appendValue(bodyContent.getString());
        tagWriter.endTag();
    }

    @Override
    protected void renderDefaultContent(TagWriter tagWriter) throws JspException {
        throw new IllegalStateException("This tag requires a bodyContent.");
    }

}
