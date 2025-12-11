package com.collaborativeeditor.module3.versioning.memento;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entity to persist document snapshots (Mementos).
 * Replaces the in-memory HashMap storage.
 * 
 * @author Arch_Force Team
 */
@Entity
@jakarta.persistence.Table(name = "document_versions")
@Data
@NoArgsConstructor
public class DocumentVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String documentId;

    @Column(nullable = false)
    private String version;

    private LocalDateTime snapshotTime;

    private String title;

    private String author;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Column(columnDefinition = "TEXT") // Store elements as JSON
    private String elementsJson;

    public DocumentVersion(String documentId, String version, String title, String author, String metadata,
            String elementsJson, LocalDateTime snapshotTime) {
        this.documentId = documentId;
        this.version = version;
        this.title = title;
        this.author = author;
        this.metadata = metadata;
        this.elementsJson = elementsJson;
        this.snapshotTime = snapshotTime;
    }
}
