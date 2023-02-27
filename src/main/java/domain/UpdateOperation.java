package domain;

import repository.IRepository;

import java.io.IOException;
import java.security.KeyException;

public class UpdateOperation<T extends Entity> extends UndoRedoOperation<T> {
    private final T updatedEntity;
    private final T oldValue;

    public UpdateOperation(IRepository<T> repository, T oldValue, T updatedEntity) {
        super(repository);
        this.oldValue = oldValue;
        this.updatedEntity = updatedEntity;
    }

    @Override
    public void undo() {
        try {
            this.repository.update(oldValue);
        } catch (KeyException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void redo() {
        try {
            this.repository.update(updatedEntity);
        } catch (KeyException | IOException e) {
            e.printStackTrace();
        }
    }
}
