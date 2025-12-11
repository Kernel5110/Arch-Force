package com.collaborativeeditor.controller;

import com.collaborativeeditor.dto.ApiResponse;
import com.collaborativeeditor.module1.creation.model.Document;
import com.collaborativeeditor.module3.versioning.command.CommandInvoker;
import com.collaborativeeditor.module3.versioning.memento.DocumentMemento;
import com.collaborativeeditor.module3.versioning.memento.DocumentOriginator;
import com.collaborativeeditor.module3.versioning.memento.MementoCaretaker;
import com.collaborativeeditor.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

/**
 * REST Controller for Module 3: Editing and Versioning.
 * Implements Command and Memento patterns.
 * 
 * @author Arch_Force Team
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VersioningController {

        private final DocumentService documentService;
        private final CommandInvoker commandInvoker;
        private final MementoCaretaker mementoCaretaker;

        /**
         * Undoes the last operation on a document.
         * POST /api/documents/undo?documentId={id}
         * 
         * Uses the Command pattern to revert the last change.
         * 
         * @param documentId document ID
         * @return updated document
         */
        @PostMapping("/documents/undo")
        public ResponseEntity<ApiResponse<Map<String, Object>>> undoLastOperation(
                        @RequestParam String documentId) {

                Document document = documentService.getDocument(documentId);
                if (document == null) {
                        return ResponseEntity
                                        .status(HttpStatus.NOT_FOUND)
                                        .body(ApiResponse.error("Document not found"));
                }

                boolean success = commandInvoker.undo(documentId);

                Map<String, Object> result = new HashMap<>();
                result.put("success", success);
                result.put("document", document);
                result.put("message", success ? "Undo successful" : "Nothing to undo");

                return ResponseEntity.ok(
                                ApiResponse.success(success ? "Undo successful" : "Nothing to undo", result));
        }

        /**
         * Redoes the last undone operation on a document.
         * POST /api/documents/redo?documentId={id}
         * 
         * Uses the Command pattern to reapply a reverted change.
         * 
         * @param documentId document ID
         * @return updated document
         */
        @PostMapping("/documents/redo")
        public ResponseEntity<ApiResponse<Map<String, Object>>> redoLastOperation(
                        @RequestParam String documentId) {

                Document document = documentService.getDocument(documentId);
                if (document == null) {
                        return ResponseEntity
                                        .status(HttpStatus.NOT_FOUND)
                                        .body(ApiResponse.error("Document not found"));
                }

                boolean success = commandInvoker.redo(documentId);

                Map<String, Object> result = new HashMap<>();
                result.put("success", success);
                result.put("document", document);
                result.put("message", success ? "Redo successful" : "Nothing to redo");

                return ResponseEntity.ok(
                                ApiResponse.success(success ? "Redo successful" : "Nothing to redo", result));
        }

        /**
         * Lists all versions of a document.
         * GET /api/versions/list?documentId={id}
         * 
         * Uses the Memento pattern to retrieve version history.
         * 
         * @param documentId document ID
         * @return list of versions
         */
        @GetMapping("/versions/list")
        public ResponseEntity<ApiResponse<List<Map<String, Object>>>> listVersions(
                        @RequestParam String documentId) {

                List<DocumentMemento> mementos = mementoCaretaker.getAllMementos(documentId);

                List<Map<String, Object>> versions = mementos.stream()
                                .map(memento -> {
                                        Map<String, Object> versionInfo = new HashMap<>();
                                        versionInfo.put("version", memento.getVersion());
                                        versionInfo.put("snapshotTime", memento.getSnapshotTime());
                                        versionInfo.put("title", memento.getTitle());
                                        versionInfo.put("author", memento.getAuthor());
                                        versionInfo.put("elementCount", memento.getElements().size());
                                        return versionInfo;
                                })
                                .collect(Collectors.toList());

                return ResponseEntity.ok(
                                ApiResponse.success("Versions retrieved successfully", versions));
        }

        /**
         * Restores a document to a specific version.
         * POST /api/versions/restore
         * 
         * Uses the Memento pattern to restore a previous state.
         * 
         * @param documentId document ID
         * @param version    version to restore
         * @return restored document
         */
        @PostMapping("/versions/restore")
        public ResponseEntity<ApiResponse<Document>> restoreVersion(
                        @RequestParam String documentId,
                        @RequestParam String version) {

                Document document = documentService.getDocument(documentId);
                if (document == null) {
                        return ResponseEntity
                                        .status(HttpStatus.NOT_FOUND)
                                        .body(ApiResponse.error("Document not found"));
                }

                DocumentMemento memento = mementoCaretaker.getMemento(documentId, version);
                if (memento == null) {
                        return ResponseEntity
                                        .status(HttpStatus.NOT_FOUND)
                                        .body(ApiResponse.error("Version not found"));
                }

                DocumentOriginator originator = new DocumentOriginator(document);
                originator.restoreFromMemento(memento);

                documentService.saveDocument(document);

                return ResponseEntity.ok(
                                ApiResponse.success("Version restored successfully", document));
        }

        /**
         * Creates a version snapshot of a document.
         * POST /api/versions/create
         * 
         * @param documentId  document ID
         * @param versionName version name
         * @return success message
         */
        @PostMapping("/versions/create")
        public ResponseEntity<ApiResponse<String>> createVersion(
                        @RequestParam String documentId,
                        @RequestParam String versionName) {

                Document document = documentService.getDocument(documentId);
                if (document == null) {
                        return ResponseEntity
                                        .status(HttpStatus.NOT_FOUND)
                                        .body(ApiResponse.error("Document not found"));
                }

                DocumentOriginator originator = new DocumentOriginator(document);
                DocumentMemento memento = originator.createMemento(versionName);
                mementoCaretaker.saveMemento(documentId, memento);

                return ResponseEntity.ok(
                                ApiResponse.success("Version created successfully", versionName));
        }
}
