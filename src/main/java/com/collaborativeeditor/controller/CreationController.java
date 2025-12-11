package com.collaborativeeditor.controller;

import com.collaborativeeditor.dto.AddElementRequest;
import com.collaborativeeditor.dto.ApiResponse;
import com.collaborativeeditor.dto.CreateDocumentRequest;
import com.collaborativeeditor.module1.creation.builder.DocumentBuilder;
import com.collaborativeeditor.module1.creation.factory.*;
import com.collaborativeeditor.module1.creation.model.*;
import com.collaborativeeditor.service.DocumentService;
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

                Element element = createElementWithFactory(
                                request.getElementType(),
                                request.getElementData());

                if (element == null) {
                        return ResponseEntity
                                        .status(HttpStatus.BAD_REQUEST)
                                        .body(ApiResponse.error("Invalid element type"));
                }

                document.addElement(element);
                documentService.saveDocument(document);

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
                }
        }

        /**
         * Creates an element using the appropriate factory (Factory Method pattern).
         * 
         * @param elementType type of element
         * @param elementData element configuration data
         * @return created element
         */
        @SuppressWarnings("unchecked")
        private Element createElementWithFactory(String elementType, Map<String, Object> elementData) {
                return switch (elementType.toLowerCase()) {
                        case "paragraph" -> new ParagraphFactory()
                                        .withContent((String) elementData.get("content"))
                                        .createElement();

                        case "image" -> new ImageFactory()
                                        .withUrl((String) elementData.get("url"))
                                        .withAltText((String) elementData.get("altText"))
                                        .createElement();

                        case "table" -> {
                                List<String> headers = (List<String>) elementData.get("headers");
                                List<List<String>> rows = (List<List<String>>) elementData.get("rows");
                                yield new TableFactory()
                                                .withHeaders(headers)
                                                .withRows(rows)
                                                .createElement();
                        }

                        case "list" -> {
                                List<String> items = (List<String>) elementData.get("items");
                                yield new ListFactory()
                                                .withItems(items)
                                                .ordered((Boolean) elementData.getOrDefault("ordered", false))
                                                .createElement();
                        }

                        case "heading" -> new HeadingFactory()
                                        .withContent((String) elementData.get("content"))
                                        .withLevel(elementData.get("level") instanceof Integer
                                                        ? (Integer) elementData.get("level")
                                                        : Integer.parseInt(elementData.get("level").toString()))
                                        .createElement();

                        default -> null;
                };
        }
}
