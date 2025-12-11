package com.collaborativeeditor.module4.collaboration.observer;

import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Subject in the Observer pattern.
 * Manages observers and notifies them of document changes.
 * 
 * @author Arch_Force Team
 */
@Component
public class DocumentSubject {

    // Map of documentId -> list of observers
    private final Map<String, List<DocumentObserver>> observers = new ConcurrentHashMap<>();

    /**
     * Attaches an observer to a document.
     * 
     * @param documentId document ID
     * @param observer   observer to attach
     */
    public void attach(String documentId, DocumentObserver observer) {
        observers.computeIfAbsent(documentId, k -> new ArrayList<>()).add(observer);
    }

    /**
     * Detaches an observer from a document.
     * 
     * @param documentId document ID
     * @param observer   observer to detach
     */
    public void detach(String documentId, DocumentObserver observer) {
        List<DocumentObserver> docObservers = observers.get(documentId);
        if (docObservers != null) {
            docObservers.remove(observer);
        }
    }

    /**
     * Notifies all observers of a document about a change.
     * 
     * @param documentId document ID
     * @param message    notification message
     */
    public void notifyObservers(String documentId, String message) {
        List<DocumentObserver> docObservers = observers.get(documentId);
        if (docObservers != null) {
            for (DocumentObserver observer : docObservers) {
                observer.update(documentId, message);
            }
        }
    }

    /**
     * Gets all observers for a document.
     * 
     * @param documentId document ID
     * @return list of observers
     */
    public List<DocumentObserver> getObservers(String documentId) {
        return new ArrayList<>(observers.getOrDefault(documentId, List.of()));
    }

    /**
     * Gets the count of observers for a document.
     * 
     * @param documentId document ID
     * @return observer count
     */
    public int getObserverCount(String documentId) {
        return observers.getOrDefault(documentId, List.of()).size();
    }
}
