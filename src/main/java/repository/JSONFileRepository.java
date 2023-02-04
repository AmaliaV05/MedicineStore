package repository;

import com.google.gson.Gson;
import domain.Entity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.KeyException;
import java.util.*;

import static java.util.Collections.max;

public class JSONFileRepository<T extends Entity> implements IRepository<T> {
    protected String filename;
    protected Gson gson;
    protected Map<Integer, T> entities = new HashMap<>();
    private int currentId;

    public JSONFileRepository(String filename, Type type) {
        this.filename = filename;
        this.gson = new Gson();

        readFile();

        try {
            currentId = max(entities.keySet()) + 1;
        } catch (NoSuchElementException ex) {
            currentId = 0;
        }
    }

    protected void readFile() {
        entities.clear();
    }

    protected void saveFile() throws IOException {
        BufferedWriter bufferedWriter = null;
        try (FileWriter out = new FileWriter(filename)) {
            try { bufferedWriter = new BufferedWriter(out);
                bufferedWriter.write(gson.toJson(entities.values()));
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
            finally {
                try {
                    if(bufferedWriter != null)
                        bufferedWriter.close();
                }
                catch(Exception ex) {
                    System.out.println("Error in closing the BufferedWriter" + ex);
                }
            }
        }
    }

    @Override
    public void create(T entity) throws KeyException, IOException {
        readFile();
        if (entity.getIdEntity() == 0) {
            entity.setIdEntity(currentId++);
        }
        if (entities.containsKey(entity.getIdEntity())) {
            throw new KeyException("The entry ID already exists!");
        }
        entities.put(entity.getIdEntity(), entity);
        saveFile();
    }

    @Override
    public T readOne(int idEntity) {
        readFile();
        return entities.get(idEntity);
    }

    @Override
    public List<T> read() {
        readFile();
        return new ArrayList<>(entities.values());
    }

    @Override
    public void update(T entity) throws KeyException, IOException {
        readFile();
        if (!entities.containsKey(entity.getIdEntity())) {
            throw new KeyException("The entry ID does not exist!");
        }
        entities.put(entity.getIdEntity(), entity);
        saveFile();
    }

    @Override
    public void delete(int idEntity) throws KeyException, IOException {
        readFile();
        if (!entities.containsKey(idEntity)) {
            throw new KeyException("The entry ID does not exist!");
        }
        entities.remove(idEntity);
        saveFile();
    }
}
