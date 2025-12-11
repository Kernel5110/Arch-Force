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

    // Use ConcurrentHashMap for thread safety in a singleton bean context
    private final java.util.Map<String, java.util.Deque<Command>> undoStacks = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.Map<String, java.util.Deque<Command>> redoStacks = new java.util.concurrent.ConcurrentHashMap<>();

    /**
     * Executes a command and adds it to the undo stack for the specific document.
     * 
     * @param documentId the document context
     * @param command    command to execute
     */
    public void executeCommand(String documentId, Command command) {
        command.execute();
        undoStacks.computeIfAbsent(documentId, k -> new java.util.ArrayDeque<>()).push(command);
        // Clear redo stack when new command is executed
        redoStacks.computeIfAbsent(documentId, k -> new java.util.ArrayDeque<>()).clear();
    }

    /**
     * Undoes the last executed command for a specific document.
     * 
     * @param documentId document ID
     * @return true if undo was successful, false if nothing to undo
     */
    public boolean undo(String documentId) {
        java.util.Deque<Command> undoStack = undoStacks.get(documentId);
        if (undoStack == null || undoStack.isEmpty()) {
            return false;
        }

        Command command = undoStack.pop();
        command.undo();
        redoStacks.computeIfAbsent(documentId, k -> new java.util.ArrayDeque<>()).push(command);
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
        if (redoStack == null || redoStack.isEmpty()) {
            return false;
        }

        Command command = redoStack.pop();
        command.execute();
        undoStacks.computeIfAbsent(documentId, k -> new java.util.ArrayDeque<>()).push(command);
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
        return (undoStack == null || undoStack.isEmpty()) ? null : undoStack.peek().getDescription();
    }

    /**
     * Gets the description of the last command that can be redone.
     * 
     * @param documentId document ID
     * @return command description or null if nothing to redo
     */
    public String getRedoDescription(String documentId) {
        java.util.Deque<Command> redoStack = redoStacks.get(documentId);
        return (redoStack == null || redoStack.isEmpty()) ? null : redoStack.peek().getDescription();
    }

    /**
     * Clears command history for a specific document.
     * 
     * @param documentId document ID
     */
    public void clear(String documentId) {
        if (undoStacks.containsKey(documentId)) {
            undoStacks.get(documentId).clear();
        }
        if (redoStacks.containsKey(documentId)) {
            redoStacks.get(documentId).clear();
        }
    }
}
