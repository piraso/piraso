package ard.piraso.api.entry;

/**
 * Defines an stack trace entry.
 */
public class StackTraceElementEntry implements Entry {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StackTraceElementEntry that = (StackTraceElementEntry) o;

        if (lineNumber != that.lineNumber) return false;
        if (declaringClass != null ? !declaringClass.equals(that.declaringClass) : that.declaringClass != null)
            return false;
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = declaringClass != null ? declaringClass.hashCode() : 0;
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + lineNumber;
        return result;
    }
}
