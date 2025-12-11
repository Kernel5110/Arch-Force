package com.collaborativeeditor.repository;

import com.collaborativeeditor.module4.collaboration.observer.PersistentCollaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for persistent storage of collaborators.
 */
@Repository
public interface CollaboratorRepository extends JpaRepository<PersistentCollaborator, Long> {

    List<PersistentCollaborator> findByDocumentId(String documentId);

    PersistentCollaborator findByObserverId(String observerId);
}
