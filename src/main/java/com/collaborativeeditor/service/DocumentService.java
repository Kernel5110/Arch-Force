package com.collaborativeeditor.service;

import com.collaborativeeditor.module1.creation.model.Document;
import com.collaborativeeditor.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for managing document storage using MariaDB.
 * 
 * @author Arch_Force Team
 */
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    /**
     * Saves a document.
     * 
     * @param document document to save
     */
    public void saveDocument(Document document) {
        documentRepository.save(document);
    }

    /**
     * Gets a document by ID.
     * 
     * @param id document ID
     * @return document or null if not found
     */
    public Document getDocument(String id) {
        return documentRepository.findById(id).orElse(null);
    }

    /**
     * Gets all documents.
     * 
     * @return list of all documents
     */
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    /**
     * Deletes a document.
     * 
     * @param id document ID
     * @return true if deleted, false if not found
     */
    public boolean deleteDocument(String id) {
        if (documentRepository.existsById(id)) {
            documentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
