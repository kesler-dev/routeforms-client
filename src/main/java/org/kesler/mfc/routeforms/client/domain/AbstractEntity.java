package org.kesler.mfc.routeforms.client.domain;

import java.util.UUID;

/**
 * Абстрактный класс сущности
 */
public abstract class AbstractEntity {

    protected UUID id;

    public AbstractEntity() {
        id = UUID.randomUUID();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractEntity that = (AbstractEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
