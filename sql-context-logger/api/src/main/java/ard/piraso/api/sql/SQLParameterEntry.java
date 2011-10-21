package ard.piraso.api.sql;

import ard.piraso.api.entry.EntryUtils;
import ard.piraso.api.entry.MethodCallEntry;
import ard.piraso.api.entry.ObjectEntry;

import java.lang.reflect.Method;

/**
 * SQL parameter entry
 */
public class SQLParameterEntry extends MethodCallEntry {
    private String name;

    private Integer index;

    public SQLParameterEntry() {}

    public SQLParameterEntry(String name, Method method, Object[] arguments) {
        this(name, method, arguments, null);
    }

    public SQLParameterEntry(String name, Method method, Object[] arguments, Object returnValue) {
        super(method);
        this.name = name;
        setArguments(EntryUtils.toEntry(arguments));
        setReturnedValue(new ObjectEntry(returnValue));
    }

    public SQLParameterEntry(Integer index, Method method, Object[] arguments) {
        this(index, method, arguments, null);
    }

    public SQLParameterEntry(Integer index, Method method, Object[] arguments, Object returnValue) {
        super(method);
        this.index = index;
        setArguments(EntryUtils.toEntry(arguments));
        setReturnedValue(new ObjectEntry(returnValue));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLParameterEntry)) return false;
        if (!super.equals(o)) return false;

        SQLParameterEntry that = (SQLParameterEntry) o;

        if (index != null ? !index.equals(that.index) : that.index != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (index != null ? index.hashCode() : 0);
        return result;
    }
}
