package ard.piraso.api.entry;

/**
 * Defines an stack trace entry.
 */
public class StackTraceElementEntry extends Entry {
    private String declaringClass;
    private String methodName;
    private String fileName;
    private int lineNumber;

    public StackTraceElementEntry() {}

    public StackTraceElementEntry(StackTraceElement element) {
        this.declaringClass = element.getClassName();
        this.fileName = element.getFileName();
        this.methodName = element.getMethodName();
        this.lineNumber = element.getLineNumber();
    }

    public String getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(String declaringClass) {
        this.declaringClass = declaringClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
