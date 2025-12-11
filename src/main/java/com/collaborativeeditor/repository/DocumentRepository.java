package com.collaborativeeditor.repository;

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
}
