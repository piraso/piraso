package org.piraso.bridge.server.taglibs;

import org.springframework.web.servlet.support.BindStatus;

/**
 * determines the status
 */
public class ControlGroupStatus {

    private BindStatus status;

    ControlGroupStatus(BindStatus status) {
        this.status = status;
    }

    public boolean isHasErrors() {
        return status.getErrors().hasFieldErrors(status.getExpression());
    }
}
