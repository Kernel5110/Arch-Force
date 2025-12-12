package com.collaborativeeditor.repository;

import java.util.List;
import com.collaborativeeditor.module1.creation.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Document entity.
 * 
 * @author Arch_Force Team
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, String> {

    /**
     * Finds all documents that are not in the recycle bin.
     */
    List<Document> findByDeletedFalse();

    /**
     * Finds all documents that are in the recycle bin.
     */
    List<Document> findByDeletedTrue();
}
