package domain;

import repository.IRepository;

public abstract class UndoRedoOperation<T extends Entity> {

    protected IRepository<T> repository;

    public UndoRedoOperation(IRepository<T> repository) {
        this.repository = repository;
    }

    public abstract void undo();
    public abstract void redo();
}
