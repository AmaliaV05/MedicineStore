package domain;

import java.util.Objects;

public class Entity {
    private int idEntity;

    public Entity(int idEntity) {
        this.idEntity = idEntity;
    }

    public Entity() { }

    public int getIdEntity() {
        return idEntity;
    }

    public void setIdEntity(int idEntity) {
        this.idEntity = idEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return idEntity == entity.idEntity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEntity);
    }
}
