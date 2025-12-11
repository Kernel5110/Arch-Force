package com.collaborativeeditor.module3.versioning.memento;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * Caretaker in the Memento pattern.
 * Manages the history of document mementos.
 * 
 * @author Arch_Force Team
 */
@Component
public class MementoCaretaker {

    // Map of documentId -> list of mementos
    private final Map<String, List<DocumentMemento>> mementoHistory = new HashMap<>();

    /**
     * Saves a memento for a document.
     * 
     * @param documentId document ID
     * @param memento    memento to save
     */
    public void saveMemento(String documentId, DocumentMemento memento) {
        mementoHistory.computeIfAbsent(documentId, k -> new ArrayList<>()).add(memento);
    }

    /**
     * Gets a specific version memento for a document.
     * 
     * @param documentId document ID
     * @param version    version to retrieve
     * @return memento or null if not found
     */
    public DocumentMemento getMemento(String documentId, String version) {
        List<DocumentMemento> mementos = mementoHistory.get(documentId);
        if (mementos == null) {
            return null;
        }

        return mementos.stream()
                .filter(m -> m.getVersion().equals(version))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets all mementos for a document.
     * 
     * @param documentId document ID
     * @return list of mementos
     */
    public List<DocumentMemento> getAllMementos(String documentId) {
        return new ArrayList<>(mementoHistory.getOrDefault(documentId, List.of()));
    }

    /**
     * Gets the latest memento for a document.
     * 
     * @param documentId document ID
     * @return latest memento or null if none exist
     */
    public DocumentMemento getLatestMemento(String documentId) {
        List<DocumentMemento> mementos = mementoHistory.get(documentId);
        if (mementos == null || mementos.isEmpty()) {
            return null;
        }
        return mementos.get(mementos.size() - 1);
    }

    /**
     * Clears all mementos for a document.
     * 
     * @param documentId document ID
     */
    public void clearHistory(String documentId) {
        mementoHistory.remove(documentId);
    }
}
