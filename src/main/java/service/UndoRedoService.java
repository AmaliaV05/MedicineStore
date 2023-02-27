package service;

import domain.Entity;
import domain.UndoRedoOperation;

import java.util.Stack;

public class UndoRedoService {

    private final Stack<UndoRedoOperation<? extends Entity>> undoStack = new Stack<>();
    private final Stack<UndoRedoOperation<? extends Entity>> redoStack = new Stack<>();

    public void addToUndo(UndoRedoOperation<? extends Entity> undoRedoOperation) {
        undoStack.push(undoRedoOperation);
        redoStack.clear();
    }

    public void undo() {
        if (!this.undoStack.isEmpty()) {
            UndoRedoOperation<? extends Entity> entityUndoRedoOperation = this.undoStack.pop();
            entityUndoRedoOperation.undo();
            redoStack.push(entityUndoRedoOperation);
        }
        else throw new IllegalStateException();
    }

    public void redo() {
        if (!this.redoStack.isEmpty()) {
            UndoRedoOperation<? extends Entity> entityUndoRedoOperation = this.redoStack.pop();
            entityUndoRedoOperation.redo();
            undoStack.push(entityUndoRedoOperation);
        }
        else throw new IllegalStateException();
    }
}
