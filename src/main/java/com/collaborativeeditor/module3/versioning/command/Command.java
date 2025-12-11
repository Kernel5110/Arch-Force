package com.collaborativeeditor.module3.versioning.command;

/**
 * Command interface for implementing the Command pattern.
 * Encapsulates document operations as objects, enabling undo/redo
 * functionality.
 * 
 * This follows the Command pattern, turning requests into stand-alone objects
 * that contain all information about the request.
 * 
 * @author Arch_Force Team
 */
public interface Command {

    /**
     * Executes the command.
     */
    void execute();

    /**
     * Undoes the command.
     */
    void undo();

    /**
     * Gets a description of this command.
     * 
     * @return command description
     */
    String getDescription();
}
