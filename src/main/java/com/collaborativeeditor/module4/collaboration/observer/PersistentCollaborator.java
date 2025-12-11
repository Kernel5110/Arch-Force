package com.collaborativeeditor.module4.collaboration.observer;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistent entity for a collaborator.
 * Stores observer state in the database.
 * 
 * @author Arch_Force Team
 */
@Entity
@jakarta.persistence.Table(name = "collaborators")
@Data
@NoArgsConstructor
public class PersistentCollaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String observerId; // UUID

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String documentId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "collaborator_notifications", joinColumns = @JoinColumn(name = "collaborator_id"))
    @Column(name = "message")
    private List<String> notifications = new ArrayList<>();

    public PersistentCollaborator(String observerId, String name, String email, String documentId) {
        this.observerId = observerId;
        this.name = name;
        this.email = email;
        this.documentId = documentId;
    }

    public void addNotification(String message) {
        this.notifications.add(message);
    }
}
