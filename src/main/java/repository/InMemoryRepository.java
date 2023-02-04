package repository;

import domain.Entity;

import java.security.KeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.max;

public class InMemoryRepository<T extends Entity> implements IRepository<T> {
    private final Map<Integer, T> storage = new HashMap<>();

    @Override
    public void create(T entity) throws KeyException {
        if (storage.size() == 0) {
            entity.setIdEntity(0);
        } else {
            entity.setIdEntity(max(storage.keySet()) + 1);
        }
        if(storage.containsKey(entity.getIdEntity())) {
            throw new KeyException("The id already exists!");
        }
        storage.put(entity.getIdEntity(), entity);
    }

    @Override
    public T readOne(int id) {
        return storage.get(id);
    }

    @Override
    public List<T> read() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void update(T entity) throws KeyException {
        if(!storage.containsKey(entity.getIdEntity())) {
            throw new KeyException("There is no entity with the given id to update!");
        }
        storage.put(entity.getIdEntity(), entity);
    }

    @Override
    public void delete(int id) throws KeyException {
        if(!storage.containsKey(id)) {
            throw new KeyException("There is no entity with the given id to delete!");
        }
        storage.remove(id);
    }
}
