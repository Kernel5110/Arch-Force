package com.collaborativeeditor.module4.collaboration.observer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete observer representing a collaborator.
 * Receives notifications about document changes.
 * 
 * @author Arch_Force Team
 */
@Slf4j
@Getter
public class Collaborator implements DocumentObserver {

    private final String observerId;
    private final String observerName;
    private final String email;
    private final List<String> notifications;

    public Collaborator(String observerId, String observerName, String email) {
        this.observerId = observerId;
        this.observerName = observerName;
        this.email = email;
        this.notifications = new ArrayList<>();
    }

    @Override
    public void update(String documentId, String message) {
        String notification = String.format(
                "Document %s: %s (notified at %s)",
                documentId,
                message,
                java.time.LocalDateTime.now());

        notifications.add(notification);
        log.info("Collaborator {} received notification: {}", observerName, notification);
    }
}
