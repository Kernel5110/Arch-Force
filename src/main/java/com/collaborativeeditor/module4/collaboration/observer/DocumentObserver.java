package com.collaborativeeditor.module4.collaboration.observer;

/**
 * Observer interface for the Observer pattern.
 * Defines the contract for collaborators to receive notifications.
 * 
 * @author Arch_Force Team
 */
public interface DocumentObserver {

    /**
     * Called when the document is updated.
     * 
     * @param documentId document ID
     * @param message    update message
     */
    void update(String documentId, String message);

    /**
     * Gets the observer's identifier.
     * 
     * @return observer ID
     */
    String getObserverId();

    /**
     * Gets the observer's name.
     * 
     * @return observer name
     */
    String getObserverName();
}
