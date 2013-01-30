package org.piraso.bridge.server.taglibs;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.servlet.tags.form.ErrorsTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

/**
 * Extension of {@link org.springframework.web.servlet.tags.form.ErrorsTag} for displaying errors on a list.
 */
public class ListErrorsTag extends ErrorsTag {

    public ListErrorsTag() {
        setElement("ul");
    }

    @Override
    protected void renderDefaultContent(TagWriter tagWriter) throws JspException {
        String[] errorMessages = getBindStatus().getErrorMessages();

        if(CollectionUtils.size(errorMessages) > 1) {
            tagWriter.startTag(getElement());
            writeDefaultAttributes(tagWriter);

            for (String errorMessage : errorMessages) {
                tagWriter.startTag("li");
                tagWriter.appendValue(errorMessage);
                tagWriter.endTag();
            }

            tagWriter.endTag();
        } else {
            tagWriter.startTag("span");
            for (String errorMessage : errorMessages) {
                tagWriter.appendValue(errorMessage);
            }
            tagWriter.endTag();
        }
    }
}
