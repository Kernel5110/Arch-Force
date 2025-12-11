package com.collaborativeeditor.controller;

import com.collaborativeeditor.dto.AddCollaboratorRequest;
import com.collaborativeeditor.dto.ApiResponse;
import com.collaborativeeditor.dto.ExportDocumentRequest;
import com.collaborativeeditor.module1.creation.model.Document;
import com.collaborativeeditor.module4.collaboration.observer.Collaborator;
import com.collaborativeeditor.module4.collaboration.observer.DocumentObserver;
import com.collaborativeeditor.module4.collaboration.observer.DocumentSubject;
import com.collaborativeeditor.module4.collaboration.strategy.ExportContext;
import com.collaborativeeditor.module4.collaboration.strategy.ExportStrategy;
import com.collaborativeeditor.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * REST Controller for Module 4: Collaboration and Export.
 * Implements Observer and Strategy patterns.
 * 
 * @author Arch_Force Team
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CollaborationController {

        private final DocumentService documentService;
        private final DocumentSubject documentSubject;
        private final ExportContext exportContext;

        /**
         * Adds a collaborator to a document.
         * POST /api/collaborators/add
         * 
         * Uses the Observer pattern to register collaborators who will be
         * notified of document changes.
         * 
         * @param request collaborator addition request
         * @return success message
         */
        @PostMapping("/collaborators/add")
        public ResponseEntity<ApiResponse<Map<String, Object>>> addCollaborator(
                        @Valid @RequestBody AddCollaboratorRequest request) {

                Document document = documentService.getDocument(request.getDocumentId());
                if (document == null) {
                        return ResponseEntity
                                        .status(HttpStatus.NOT_FOUND)
                                        .body(ApiResponse.error("Document not found"));
                }

                // Create new collaborator (observer)
                String collaboratorId = UUID.randomUUID().toString();
                Collaborator collaborator = new Collaborator(
                                collaboratorId,
                                request.getCollaboratorName(),
                                request.getEmail());

                // Attach observer to document
                documentSubject.attach(request.getDocumentId(), collaborator);

                // Notify all collaborators
                documentSubject.notifyObservers(
                                request.getDocumentId(),
                                collaborator.getObserverName() + " joined the document");

                Map<String, Object> result = new HashMap<>();
                result.put("collaboratorId", collaboratorId);
                result.put("collaboratorName", request.getCollaboratorName());
                result.put("totalCollaborators", documentSubject.getObserverCount(request.getDocumentId()));

                return ResponseEntity.ok(
                                ApiResponse.success("Collaborator added successfully", result));
        }

        /**
         * Gets all collaborators for a document.
         * GET /api/collaborators/list?documentId={id}
         * 
         * @param documentId document ID
         * @return list of collaborators
         */
        @GetMapping("/collaborators/list")
        public ResponseEntity<ApiResponse<List<Map<String, String>>>> listCollaborators(
                        @RequestParam String documentId) {

                List<DocumentObserver> observers = documentSubject.getObservers(documentId);

                List<Map<String, String>> collaborators = observers.stream()
                                .map(observer -> {
                                        Map<String, String> info = new HashMap<>();
                                        info.put("id", observer.getObserverId());
                                        info.put("name", observer.getObserverName());
                                        return info;
                                })
                                .collect(Collectors.toList());

                return ResponseEntity.ok(
                                ApiResponse.success("Collaborators retrieved successfully", collaborators));
        }

        /**
         * Exports a document in a specific format.
         * POST /api/export/document
         * 
         * Uses the Strategy pattern to select the appropriate export format
         * at runtime without modifying the export logic.
         * 
         * @param request export request
         * @return exported document content
         */
        @PostMapping("/export/document")
        public ResponseEntity<ApiResponse<Map<String, String>>> exportDocument(
                        @Valid @RequestBody ExportDocumentRequest request) {

                Document document = documentService.getDocument(request.getDocumentId());
                if (document == null) {
                        return ResponseEntity
                                        .status(HttpStatus.NOT_FOUND)
                                        .body(ApiResponse.error("Document not found"));
                }

                try {
                        // Set export strategy based on requested format
                        exportContext.setStrategyByFormat(request.getFormat());

                        // Execute export using the selected strategy
                        byte[] exportedContent = exportContext.executeExport(document);
                        String base64Content = Base64.getEncoder().encodeToString(exportedContent);

                        Map<String, String> result = new HashMap<>();
                        result.put("format", request.getFormat());
                        result.put("content", base64Content);
                        result.put("documentTitle", document.getTitle());
                        result.put("encoding", "base64"); // Inform client

                        // Notify collaborators about the export
                        documentSubject.notifyObservers(
                                        request.getDocumentId(),
                                        "Document exported to " + request.getFormat().toUpperCase());

                        return ResponseEntity.ok(
                                        ApiResponse.success("Document exported successfully", result));

                } catch (IllegalArgumentException e) {
                        return ResponseEntity
                                        .status(HttpStatus.BAD_REQUEST)
                                        .body(ApiResponse.error("Unsupported export format: " + request.getFormat()));
                }
        }

        /**
         * Gets all available export formats.
         * GET /api/export/formats
         * 
         * @return list of available formats
         */
        @GetMapping("/export/formats")
        public ResponseEntity<ApiResponse<List<Map<String, String>>>> getExportFormats() {
                Map<String, ExportStrategy> formats = exportContext.getAvailableFormats();

                List<Map<String, String>> formatList = formats.entrySet().stream()
                                .map(entry -> {
                                        Map<String, String> formatInfo = new HashMap<>();
                                        formatInfo.put("format", entry.getKey());
                                        formatInfo.put("name", entry.getValue().getFormatName());
                                        formatInfo.put("extension", entry.getValue().getFileExtension());
                                        return formatInfo;
                                })
                                .collect(Collectors.toList());

                return ResponseEntity.ok(
                                ApiResponse.success("Export formats retrieved successfully", formatList));
        }
}
