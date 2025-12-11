package com.collaborativeeditor.module4.collaboration.observer;

import com.collaborativeeditor.repository.CollaboratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Subject in the Observer pattern.
 * Manages observers and notifies them of document changes using persistent
 * storage.
 * 
 * @author Arch_Force Team
 */
@Component
@RequiredArgsConstructor
public class DocumentSubject {

    private final CollaboratorRepository collaboratorRepository;

    /**
     * Attaches an observer to a document.
     * 
     * @param documentId document ID
     * @param observer   observer to attach
     */
    public void attach(String documentId, DocumentObserver observer) {
        // Check if already exists to avoid duplicates (optional, strictly logic
        // dependent)
        // For now, simpler: just create the entity.
        // In a real app we'd check if user is already collab.

        PersistentCollaborator entity = new PersistentCollaborator(
                observer.getObserverId(),
                observer.getObserverName(),
                // Casting is ugly but we know the concrete type in this context usually
                // Ideally DocumentObserver interface would return email or we'd change the
                // method signature
                // For now, we assume it's created from the controller with fresh data.
                // Wait, observer is passed in.
                // Let's rely on the interface methods only if possible. But interface only has
                // ID/Name.
                // We need to modify the interface or the caller.
                // The Caller (CollaborationController) creates 'Collaborator' object.
                // So we can extract data from it.
                observer.getObserverName(), // Hack: we don't have email in interface.
                documentId);

        // Fix: The interface doesn't have email. We should persist what we have or
        // cast.
        if (observer instanceof Collaborator) {
            entity.setEmail(((Collaborator) observer).getEmail());
        } else {
            entity.setEmail("unknown@example.com");
        }

        collaboratorRepository.save(entity);
    }

    /**
     * Detaches an observer from a document.
     * 
     * @param documentId document ID
     * @param observer   observer to detach
     */
    public void detach(String documentId, DocumentObserver observer) {
        PersistentCollaborator entity = collaboratorRepository.findByObserverId(observer.getObserverId());
        if (entity != null) {
            collaboratorRepository.delete(entity);
        }
    }

    /**
     * Notifies all observers of a document about a change.
     * 
     * @param documentId document ID
     * @param message    notification message
     */
    public void notifyObservers(String documentId, String message) {
        List<PersistentCollaborator> collaborators = collaboratorRepository.findByDocumentId(documentId);

        for (PersistentCollaborator entity : collaborators) {
            // Update entity state (notifications history)
            entity.addNotification(
                    String.format("Document %s: %s (at %s)", documentId, message, java.time.LocalDateTime.now()));
            collaboratorRepository.save(entity); // Persist the notification

            // In a real real-time app, here we would push via WebSocket.
            // But persisting the "Notification" log is what 'Collaborator.update()' did
            // previously in memory.
        }
    }

    /**
     * Gets all observers for a document.
     * 
     * @param documentId document ID
     * @return list of observers
     */
    public List<DocumentObserver> getObservers(String documentId) {
        return collaboratorRepository.findByDocumentId(documentId).stream()
                .map(entity -> new Collaborator(entity.getObserverId(), entity.getName(), entity.getEmail()))
                .collect(Collectors.toList());
    }

    /**
     * Gets the count of observers for a document.
     * 
     * @param documentId document ID
     * @return observer count
     */
    public int getObserverCount(String documentId) {
        return collaboratorRepository.findByDocumentId(documentId).size();
    }
}
