package com.collaborativeeditor.module3.versioning.command;

import org.springframework.stereotype.Component;

/**
 * Invoker in the Command pattern.
 * Manages command history and executes undo/redo operations.
 * 
 * @author Arch_Force Team
 */
@Component
public class CommandInvoker {

    // Use ConcurrentHashMap for thread safety in a singleton bean context
    private final java.util.Map<String, java.util.Deque<Command>> undoStacks = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.Map<String, java.util.Deque<Command>> redoStacks = new java.util.concurrent.ConcurrentHashMap<>();

    private static final int MAX_HISTORY_SIZE = 50;

    /**
     * Executes a command and adds it to the undo stack for the specific document.
     * 
     * @param documentId the document context
     * @param command    command to execute
     */
    public void executeCommand(String documentId, Command command) {
        command.execute();
        java.util.Deque<Command> undoStack = undoStacks.computeIfAbsent(documentId, k -> new java.util.ArrayDeque<>());

        synchronized (undoStack) {
            undoStack.push(command);

            // Prevent memory leaks by limiting history size
            if (undoStack.size() > MAX_HISTORY_SIZE) {
                undoStack.removeLast();
            }
        }

        // Clear redo stack when new command is executed.
        // We must use computeIfAbsent to ensure we lock the authoritative stack
        // instance
        // to prevent race conditions with concurrent undo/redo operations.
        java.util.Deque<Command> redoStack = redoStacks.computeIfAbsent(documentId, k -> new java.util.ArrayDeque<>());
        synchronized (redoStack) {
            redoStack.clear();
        }
    }

    /**
     * Undoes the last executed command for a specific document.
     * 
     * @param documentId document ID
     * @return true if undo was successful, false if nothing to undo
     */
    public boolean undo(String documentId) {
        java.util.Deque<Command> undoStack = undoStacks.get(documentId);
        if (undoStack == null) {
            return false;
        }

        Command command;
        synchronized (undoStack) {
            if (undoStack.isEmpty()) {
                return false;
            }
            command = undoStack.pop();
        }

        // Execute undo outside the lock to prevent deadlocks
        command.undo();

        java.util.Deque<Command> redoStack = redoStacks.computeIfAbsent(documentId, k -> new java.util.ArrayDeque<>());
        synchronized (redoStack) {
            redoStack.push(command);
        }
        return true;
    }

    /**
     * Redoes the last undone command for a specific document.
     * 
     * @param documentId document ID
     * @return true if redo was successful, false if nothing to redo
     */
    public boolean redo(String documentId) {
        java.util.Deque<Command> redoStack = redoStacks.get(documentId);
        if (redoStack == null) {
            return false;
        }

        Command command;
        synchronized (redoStack) {
            if (redoStack.isEmpty()) {
                return false;
            }
            command = redoStack.pop();
        }

        // Execute redo outside the lock
        command.execute();

        java.util.Deque<Command> undoStack = undoStacks.computeIfAbsent(documentId, k -> new java.util.ArrayDeque<>());
        synchronized (undoStack) {
            undoStack.push(command);
        }
        return true;
    }

    /**
     * Gets the description of the last command that can be undone.
     * 
     * @param documentId document ID
     * @return command description or null if nothing to undo
     */
    public String getUndoDescription(String documentId) {
        java.util.Deque<Command> undoStack = undoStacks.get(documentId);
        if (undoStack == null)
            return null;

        synchronized (undoStack) {
            return undoStack.isEmpty() ? null : undoStack.peek().getDescription();
        }
    }

    /**
     * Gets the description of the last command that can be redone.
     * 
     * @param documentId document ID
     * @return command description or null if nothing to redo
     */
    public String getRedoDescription(String documentId) {
        java.util.Deque<Command> redoStack = redoStacks.get(documentId);
        if (redoStack == null)
            return null;

        synchronized (redoStack) {
            return redoStack.isEmpty() ? null : redoStack.peek().getDescription();
        }
    }

    /**
     * Clears command history for a specific document.
     * Removes the history entries entirely to prevent memory leaks.
     * 
     * @param documentId document ID
     */
    public void clear(String documentId) {
        undoStacks.remove(documentId);
        redoStacks.remove(documentId);
    }
}
