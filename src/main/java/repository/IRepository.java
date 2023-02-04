package repository;

import domain.Entity;

import java.io.IOException;
import java.security.KeyException;
import java.util.List;

public interface IRepository<T extends Entity> {

    void create(T entity) throws KeyException, IOException;

    T readOne(int id);

    List<T> read();

    void update(T entity) throws KeyException, IOException;

    void delete(int id) throws KeyException, IOException;
}
