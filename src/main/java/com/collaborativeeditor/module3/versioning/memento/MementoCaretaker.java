package com.collaborativeeditor.module3.versioning.memento;

import com.collaborativeeditor.module1.creation.model.Element;
import com.collaborativeeditor.repository.DocumentVersionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Caretaker in the Memento pattern.
 * Manages the history of document mementos using persistent storage.
 * 
 * @author Arch_Force Team
 */
@Component
@RequiredArgsConstructor
public class MementoCaretaker {

    private final DocumentVersionRepository versionRepository;
    private final ObjectMapper objectMapper;

    /**
     * Saves a memento for a document to the database.
     * 
     * @param documentId document ID
     * @param memento    memento to save
     */
    public void saveMemento(String documentId, DocumentMemento memento) {
        try {
            String elementsJson = objectMapper.writeValueAsString(memento.getElements());

            DocumentVersion versionEntity = new DocumentVersion(
                    documentId,
                    memento.getVersion(),
                    memento.getTitle(),
                    memento.getAuthor(),
                    memento.getMetadata(),
                    elementsJson,
                    memento.getSnapshotTime());

            versionRepository.save(versionEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize document state", e);
        }
    }

    /**
     * Gets a specific version memento for a document from the database.
     * 
     * @param documentId document ID
     * @param version    version to retrieve
     * @return memento or null if not found
     */
    public DocumentMemento getMemento(String documentId, String version) {
        return versionRepository.findByDocumentIdAndVersion(documentId, version)
                .map(this::convertToMemento)
                .orElse(null);
    }

    /**
     * Gets all mementos for a document from the database.
     * 
     * @param documentId document ID
     * @return list of mementos
     */
    public List<DocumentMemento> getAllMementos(String documentId) {
        return versionRepository.findByDocumentIdOrderBySnapshotTimeDesc(documentId)
                .stream()
                .map(this::convertToMemento)
                .collect(Collectors.toList());
    }

    /**
     * Gets the latest memento for a document.
     * 
     * @param documentId document ID
     * @return latest memento or null if none exist
     */
    public DocumentMemento getLatestMemento(String documentId) {
        List<DocumentVersion> versions = versionRepository.findByDocumentIdOrderBySnapshotTimeDesc(documentId);
        if (versions.isEmpty()) {
            return null;
        }
        return convertToMemento(versions.get(0));
    }

    /**
     * Clears all mementos for a document.
     * 
     * @param documentId document ID
     */
    public void clearHistory(String documentId) {
        List<DocumentVersion> versions = versionRepository.findByDocumentIdOrderBySnapshotTimeDesc(documentId);
        versionRepository.deleteAll(versions);
    }

    /**
     * Deletes a specific memento (version) of a document.
     * 
     * @param documentId document ID
     * @param version    version to delete
     * @return true if deleted, false if not found
     */
    public boolean deleteMemento(String documentId, String version) {
        Optional<DocumentVersion> versionEntity = versionRepository.findByDocumentIdAndVersion(documentId, version);
        if (versionEntity.isPresent()) {
            versionRepository.delete(versionEntity.get());
            return true;
        }
        return false;
    }

    private DocumentMemento convertToMemento(DocumentVersion entity) {
        try {
            List<Element> elements = objectMapper.readValue(
                    entity.getElementsJson(),
                    new TypeReference<List<Element>>() {
                    });

            // Reconstruct the Memento
            // Note: We use the constructor. Memento fields are final.
            return new DocumentMemento(
                    entity.getDocumentId(), // reusing memento ID as document ID for now as per original code
                    entity.getTitle(),
                    entity.getAuthor(),
                    elements,
                    entity.getMetadata(),
                    entity.getVersion());
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize document state for version " + entity.getVersion(), e);
        }
    }
}
