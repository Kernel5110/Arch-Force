package com.collaborativeeditor.controller;

import com.collaborativeeditor.dto.AddElementRequest;
import com.collaborativeeditor.dto.ApiResponse;
import com.collaborativeeditor.dto.CreateDocumentRequest;

import com.collaborativeeditor.module1.creation.builder.DocumentBuilder;

import com.collaborativeeditor.module1.creation.model.*;
import com.collaborativeeditor.service.DocumentService;
import com.collaborativeeditor.module4.collaboration.observer.DocumentSubject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Module 1: Document Creation.
 * Implements Factory Method and Builder patterns.
 * 
 * @author Arch_Force Team
 */
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class CreationController {

        private final DocumentBuilder documentBuilder;
        private final DocumentService documentService;
        private final DocumentSubject documentSubject;

        /**
         * Creates a new document using the Builder pattern.
         * POST /api/documents/create
         * 
         * @param request document creation request
         * @return created document
         */
        @PostMapping("/create")
        public ResponseEntity<ApiResponse<Document>> createDocument(
                        @Valid @RequestBody CreateDocumentRequest request) {

                Document document = documentBuilder
                                .reset()
                                .withTitle(request.getTitle())
                                .withAuthor(request.getAuthor())
                                .withMetadata(request.getMetadata())
                                .build();

                documentService.saveDocument(document);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(ApiResponse.success("Document created successfully", document));
        }

        /**
         * Adds an element to a document using Factory Method pattern.
         * POST /api/documents/add-element
         * 
         * @param request element addition request
         * @return updated document
         */
        @PostMapping("/add-element")
        public ResponseEntity<ApiResponse<Document>> addElement(
                        @Valid @RequestBody AddElementRequest request) {

                Document document = documentService.getDocument(request.getDocumentId());
                if (document == null) {
                        return ResponseEntity
                                        .status(HttpStatus.NOT_FOUND)
                                        .body(ApiResponse.error("Document not found"));
                }

                String type = request.getElementType();
                Map<String, Object> data = request.getElementData();

                if (type == null || data == null) {
                        return ResponseEntity
                                        .status(HttpStatus.BAD_REQUEST)
                                        .body(ApiResponse.error("Invalid element data"));
                }

                Element element = mapRawDataToElement(type, data);

                document.addElement(element);
                documentService.saveDocument(document);

                // Notify observers for real-time updates
                documentSubject.notifyObservers(request.getDocumentId(), "Element added: " + type);

                return ResponseEntity.ok(
                                ApiResponse.success("Element added successfully", document));
        }

        /**
         * Gets a document by ID.
         * GET /api/documents/{id}
         * 
         * @param id document ID
         * @return document
         */
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<Document>> getDocument(@PathVariable String id) {
                Document document = documentService.getDocument(id);

                if (document == null) {
                        return ResponseEntity
                                        .status(HttpStatus.NOT_FOUND)
                                        .body(ApiResponse.error("Document not found"));
                }

                return ResponseEntity.ok(
                                ApiResponse.success("Document retrieved successfully", document));
        }

        /**
         * Gets all documents.
         * GET /api/documents
         * 
         * @return list of documents
         */
        @GetMapping
        public ResponseEntity<ApiResponse<java.util.List<Document>>> getAllDocuments() {
                return ResponseEntity.ok(
                                ApiResponse.success("Documents retrieved successfully",
                                                documentService.getAllDocuments()));
        }

        /**
         * Deletes an element from a document.
         * POST /api/documents/delete-element
         *
         * @param request delete request containing documentId and elementId
         * @return updated document
         */
        @PostMapping("/delete-element")
        public ResponseEntity<ApiResponse<Document>> deleteElement(@RequestBody Map<String, String> request) {
                String documentId = request.get("documentId");
                String elementId = request.get("elementId");

                if (documentId == null || elementId == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                        .body(ApiResponse.error("Missing documentId or elementId"));
                }

                Document document = documentService.getDocument(documentId);
                if (document == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body(ApiResponse.error("Document not found"));
                }

                boolean removed = document.getElements().removeIf(e -> e.getId().equals(Long.parseLong(elementId)));

                if (removed) {
                        document.setLastModified(java.time.LocalDateTime.now());
                        documentService.saveDocument(document);
                        documentSubject.notifyObservers(documentId, "Element deleted");
                        return ResponseEntity.ok(ApiResponse.success("Element deleted successfully", document));
                } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body(ApiResponse.error("Element not found"));
                }
        }

        /**
         * Updates an existing element in a document.
         * POST /api/documents/update-element
         * 
         * @param request element update request
         * @return updated document
         */
        @PostMapping("/update-element")
        public ResponseEntity<ApiResponse<Document>> updateElement(
                        @Valid @RequestBody com.collaborativeeditor.dto.UpdateElementRequest request) {

                Document document = documentService.getDocument(request.getDocumentId());
                if (document == null) {
                        return ResponseEntity
                                        .status(HttpStatus.NOT_FOUND)
                                        .body(ApiResponse.error("Document not found"));
                }

                Element elementToUpdate = document.getElements().stream()
                                .filter(e -> e.getId().equals(request.getElementId()))
                                .findFirst()
                                .orElse(null);

                if (elementToUpdate == null) {
                        return ResponseEntity
                                        .status(HttpStatus.NOT_FOUND)
                                        .body(ApiResponse.error("Element not found"));
                }

                updateElementData(elementToUpdate, request.getElementData());
                document.setLastModified(java.time.LocalDateTime.now()); // Force version increment
                documentService.saveDocument(document);

                return ResponseEntity.ok(
                                ApiResponse.success("Element updated successfully", document));
        }

        private void updateElementData(Element element, Map<String, Object> data) {
                if (element instanceof Paragraph paragraph && data.containsKey("content")) {
                        paragraph.setContent((String) data.get("content"));
                } else if (element instanceof Heading heading) {
                        if (data.containsKey("content"))
                                heading.setContent((String) data.get("content"));
                        if (data.containsKey("level"))
                                heading.setLevel(data.get("level") instanceof Integer ? (Integer) data.get("level")
                                                : Integer.parseInt(data.get("level").toString()));
                } else if (element instanceof Image image) {
                        if (data.containsKey("url"))
                                image.setUrl((String) data.get("url"));
                        if (data.containsKey("altText"))
                                image.setAltText((String) data.get("altText"));
                } else if (element instanceof ListElement list) {
                        if (data.containsKey("items")) {
                                @SuppressWarnings("unchecked")
                                List<String> items = (List<String>) data.get("items");
                                list.setItems(items);
                        }
                        if (data.containsKey("ordered"))
                                list.setOrdered((Boolean) data.getOrDefault("ordered", false));
                } else if (element instanceof CodeBlock code) {
                        if (data.containsKey("content"))
                                code.setContent((String) data.get("content"));
                        if (data.containsKey("language"))
                                code.setLanguage((String) data.get("language"));
                }
        }

        /**
         * Creates an element using the appropriate factory (Factory Method pattern).
         * 
         * @param elementType type of element
         * @param elementData element configuration data
         * @return created element
         */

        private Element mapRawDataToElement(String type, Map<String, Object> data) {
                switch (type.toLowerCase()) {
                        case "paragraph":
                                Paragraph p = new Paragraph();
                                if (data.containsKey("content")) {
                                        p.setContent((String) data.get("content"));
                                }
                                return p;
                        case "heading":
                                Heading h = new Heading();
                                if (data.containsKey("content")) {
                                        h.setContent((String) data.get("content"));
                                }
                                if (data.containsKey("level")) {
                                        Object levelObj = data.get("level");
                                        h.setLevel(levelObj instanceof Integer ? (Integer) levelObj
                                                        : Integer.parseInt(levelObj.toString()));
                                } else {
                                        h.setLevel(1);
                                }
                                return h;
                        case "image":
                                Image i = new Image();
                                if (data.containsKey("url")) {
                                        i.setUrl((String) data.get("url"));
                                }
                                if (data.containsKey("altText")) {
                                        i.setAltText((String) data.get("altText"));
                                }
                                return i;
                        case "list":
                                ListElement l = new ListElement();
                                if (data.containsKey("items")) {
                                        @SuppressWarnings("unchecked")
                                        List<String> items = (List<String>) data.get("items");
                                        l.setItems(items);
                                }
                                if (data.containsKey("ordered")) {
                                        l.setOrdered((Boolean) data.getOrDefault("ordered", false));
                                }
                                return l;
                        case "code":
                                CodeBlock c = new CodeBlock();
                                if (data.containsKey("content"))
                                        c.setContent((String) data.get("content"));
                                if (data.containsKey("language"))
                                        c.setLanguage((String) data.get("language"));
                                return c;
                        default:
                                Paragraph fallback = new Paragraph();
                                if (data.containsKey("content")) {
                                        fallback.setContent((String) data.get("content"));
                                }
                                return fallback;
                }
        }

        /**
         * Deletes a document by ID.
         * DELETE /api/documents/{id}
         * 
         * @param id document ID
         * @return success message
         */
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<String>> deleteDocument(@PathVariable String id) {
                boolean deleted = documentService.deleteDocument(id);

                if (deleted) {
                        return ResponseEntity.ok(
                                        ApiResponse.success("Document deleted successfully", id));
                } else {
                        return ResponseEntity
                                        .status(HttpStatus.NOT_FOUND)
                                        .body(ApiResponse.error("Document not found"));
                }
        }

        /**
         * Gets documents in the recycle bin.
         * GET /api/documents/recycle-bin
         *
         * @return list of deleted documents
         */
        @GetMapping("/recycle-bin")
        public ResponseEntity<ApiResponse<List<Document>>> getRecycleBin() {
                return ResponseEntity.ok(
                                ApiResponse.success("Recycle bin retrieved successfully",
                                                documentService.getRecycleBinDocuments()));
        }

        /**
         * Restores a document from the recycle bin.
         * POST /api/documents/restore/{id}
         *
         * @param id document ID
         * @return success message
         */
        @PostMapping("/restore/{id}")
        public ResponseEntity<ApiResponse<String>> restoreDocument(@PathVariable String id) {
                boolean restored = documentService.restoreDocument(id);
                if (restored) {
                        return ResponseEntity.ok(ApiResponse.success("Document restored successfully", id));
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Document not found"));
        }

        /**
         * Permanently deletes a document.
         * DELETE /api/documents/permanent/{id}
         *
         * @param id document ID
         * @return success message
         */
        @DeleteMapping("/permanent/{id}")
        public ResponseEntity<ApiResponse<String>> permanentDeleteDocument(@PathVariable String id) {
                boolean deleted = documentService.permanentDeleteDocument(id);
                if (deleted) {
                        return ResponseEntity.ok(ApiResponse.success("Document permanently deleted", id));
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Document not found"));
        }
}
