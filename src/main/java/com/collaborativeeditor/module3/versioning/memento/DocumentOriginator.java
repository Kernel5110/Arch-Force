package com.collaborativeeditor.module3.versioning.memento;

import com.collaborativeeditor.module1.creation.model.Document;
import java.util.ArrayList;

/**
 * Originator in the Memento pattern.
 * Creates and restores mementos of document state.
 * 
 * @author Arch_Force Team
 */
public class DocumentOriginator {

    private Document document;

    public DocumentOriginator(Document document) {
        this.document = document;
    }

    /**
     * Sets the current document.
     * 
     * @param document document to manage
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * Gets the current document.
     * 
     * @return current document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Creates a memento from the current document state.
     * 
     * @param version version identifier
     * @return memento snapshot
     */
    public DocumentMemento createMemento(String version) {
        return new DocumentMemento(
                document.getId(),
                document.getTitle(),
                document.getAuthor(),
                document.getElements(),
                document.getMetadata(),
                version);
    }

    /**
     * Restores the document from a memento.
     * 
     * @param memento memento to restore from
     */
    public void restoreFromMemento(DocumentMemento memento) {
        document.setId(memento.getId());
        document.setTitle(memento.getTitle());
        document.setAuthor(memento.getAuthor());
        document.setElements(new ArrayList<>(memento.getElements()));
        document.setMetadata(memento.getMetadata());
    }
}
