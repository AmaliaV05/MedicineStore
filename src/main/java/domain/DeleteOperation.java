package domain;

import repository.IRepository;

import java.io.IOException;
import java.security.KeyException;

public class DeleteOperation<T extends Entity> extends UndoRedoOperation<T> {

    private final T oldValue;

    public DeleteOperation(IRepository<T> repository, T oldValue) {
        super(repository);
        this.oldValue = oldValue;
    }

    @Override
    public void undo() {
        try {
            this.repository.create(oldValue);
        } catch (KeyException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void redo() {
        try {
            this.repository.delete(oldValue.getIdEntity());
        } catch (KeyException | IOException e) {
            e.printStackTrace();
        }
    }
}
