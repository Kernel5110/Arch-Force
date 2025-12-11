package com.collaborativeeditor.module3.versioning.command;

import org.springframework.stereotype.Component;
import java.util.Stack;

/**
 * Invoker in the Command pattern.
 * Manages command history and executes undo/redo operations.
 * 
 * @author Arch_Force Team
 */
@Component
public class CommandInvoker {

    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    /**
     * Executes a command and adds it to the undo stack.
     * 
     * @param command command to execute
     */
    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Clear redo stack when new command is executed
    }

    /**
     * Undoes the last executed command.
     * 
     * @return true if undo was successful, false if nothing to undo
     */
    public boolean undo() {
        if (undoStack.isEmpty()) {
            return false;
        }

        Command command = undoStack.pop();
        command.undo();
        redoStack.push(command);
        return true;
    }

    /**
     * Redoes the last undone command.
     * 
     * @return true if redo was successful, false if nothing to redo
     */
    public boolean redo() {
        if (redoStack.isEmpty()) {
            return false;
        }

        Command command = redoStack.pop();
        command.execute();
        undoStack.push(command);
        return true;
    }

    /**
     * Gets the description of the last command that can be undone.
     * 
     * @return command description or null if nothing to undo
     */
    public String getUndoDescription() {
        return undoStack.isEmpty() ? null : undoStack.peek().getDescription();
    }

    /**
     * Gets the description of the last command that can be redone.
     * 
     * @return command description or null if nothing to redo
     */
    public String getRedoDescription() {
        return redoStack.isEmpty() ? null : redoStack.peek().getDescription();
    }

    /**
     * Clears all command history.
     */
    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
}
