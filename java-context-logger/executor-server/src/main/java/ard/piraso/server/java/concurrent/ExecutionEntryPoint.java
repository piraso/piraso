package ard.piraso.server.java.concurrent;

import ard.piraso.server.PirasoEntryPoint;

/**
 * Executor service entry point
 */
public class ExecutionEntryPoint implements PirasoEntryPoint {

    private String beanName;

    public ExecutionEntryPoint(String beanName) {
        this.beanName = beanName;
    }

    public String getPath() {
        return "java:/executor/service/" + beanName;
    }

    public String getRemoteAddr() {
        return "executor.service." + beanName;
    }
}
