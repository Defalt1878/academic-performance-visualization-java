package com.defalt.apv.report;

import java.util.Objects;

public abstract class Identifiable {
    private Integer id;

    protected Identifiable() {
        this.id = null;
    }

    public final boolean hasId() {
        return Objects.nonNull(id);
    }

    public final int getId() throws UnsupportedOperationException {
        if (this.id == null)
            throw new UnsupportedOperationException("Id has not been set yet!");
        return this.id;
    }

    public final void setId(int value) throws UnsupportedOperationException {
        if (this.id != null)
            throw new UnsupportedOperationException("Id has already been set!");
        this.id = value;
    }
}
