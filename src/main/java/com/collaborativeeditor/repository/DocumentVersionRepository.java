package com.collaborativeeditor.repository;

import com.collaborativeeditor.module3.versioning.memento.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for persistent storage of document versions.
 */
@Repository
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Long> {

    /**
     * Finds all versions for a document, ordered by newest first.
     */
    List<DocumentVersion> findByDocumentIdOrderBySnapshotTimeDesc(String documentId);

    /**
     * Finds a specific version of a document.
     */
    Optional<DocumentVersion> findByDocumentIdAndVersion(String documentId, String version);
}
