package org.piraso.server;

import org.piraso.api.NextGenerator;

public enum PirasoContextIDGenerator implements NextGenerator<Long> {
    INSTANCE;

    protected NextGenerator<Long> idGenerator;

    public NextGenerator<Long> getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(NextGenerator<Long> idGenerator) {
        this.idGenerator = idGenerator;
    }

    public Long next() {
        return idGenerator.next();
    }
}
