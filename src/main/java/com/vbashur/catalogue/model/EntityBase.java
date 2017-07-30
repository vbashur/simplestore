package com.vbashur.catalogue.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class EntityBase implements Serializable {

    @Id
    @Column(unique = true, nullable = false, updatable = false, length = 36)
    private UUID uuid = UUID.randomUUID();

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityBase)) return false;

        EntityBase that = (EntityBase) o;

        return uuid.equals(that.uuid);

    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() { return ToStringBuilder.reflectionToString(this); }
}
