package ard.piraso.server.sql;

/**
 * Sequential ID Generator.
 */
public class IDGenerator {

    private long id = 0;

    public long next() {
        return id++;
    }
}
