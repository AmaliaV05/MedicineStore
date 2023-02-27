package domain;

import repository.IRepository;

import java.io.IOException;
import java.security.KeyException;

public class AddOperation<T extends Entity> extends UndoRedoOperation<T> {
    private final T addedEntity;

    public AddOperation(IRepository<T> repository, T addedEntity) {
        super(repository);
        this.addedEntity = addedEntity;
    }

    @Override
    public void undo() {
        try {
            this.repository.delete(addedEntity.getIdEntity());
        } catch (KeyException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void redo() {
        try {
            this.repository.create(addedEntity);
        } catch (KeyException | IOException e) {
            e.printStackTrace();
        }
    }
}
