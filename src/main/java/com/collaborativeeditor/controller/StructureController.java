package com.collaborativeeditor.controller;

import com.collaborativeeditor.dto.ApiResponse;
import com.collaborativeeditor.dto.ApplyStyleRequest;
import com.collaborativeeditor.dto.DocumentStructureResponse;
import com.collaborativeeditor.module1.creation.model.Document;
import com.collaborativeeditor.module1.creation.model.Element;
import com.collaborativeeditor.module2.structure.composite.DocumentComponent;
import com.collaborativeeditor.module2.structure.composite.ElementLeaf;
import com.collaborativeeditor.module2.structure.composite.Section;
import com.collaborativeeditor.module2.structure.decorator.*;
import com.collaborativeeditor.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Module 2: Structure and Styles.
 * Implements Composite and Decorator patterns.
 * 
 * @author Arch_Force Team
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StructureController {

    private final DocumentService documentService;

    /**
     * Gets the hierarchical structure of a document.
     * GET /api/documents/structure?documentId={id}
     * 
     * Uses the Composite pattern to represent the document hierarchy.
     * 
     * @param documentId document ID
     * @return document structure
     */
    @GetMapping("/documents/structure")
    public ResponseEntity<ApiResponse<DocumentStructureResponse>> getDocumentStructure(
            @RequestParam String documentId) {

        Document document = documentService.getDocument(documentId);
        if (document == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Document not found"));
        }

        // Build composite structure from document elements
        Section root = new Section(document.getTitle());

        for (Element element : document.getElements()) {
            ElementLeaf leaf = new ElementLeaf(element);
            root.add(leaf);
        }

        DocumentStructureResponse response = new DocumentStructureResponse(
                document.getId(),
                document.getTitle(),
                root);

        return ResponseEntity.ok(
                ApiResponse.success("Document structure retrieved successfully", response));
    }

    /**
     * Applies styles to a document element using the Decorator pattern.
     * POST /api/styles/apply
     * 
     * Dynamically wraps elements with style decorators without modifying
     * the original element classes.
     * 
     * @param request style application request
     * @return updated document
     */
    @PostMapping("/styles/apply")
    public ResponseEntity<ApiResponse<Document>> applyStyles(
            @Valid @RequestBody ApplyStyleRequest request) {

        Document document = documentService.getDocument(request.getDocumentId());
        if (document == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Document not found"));
        }

        if (request.getElementIndex() < 0 ||
                request.getElementIndex() >= document.getElements().size()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Invalid element index"));
        }

        // Get the element to style
        Element element = document.getElements().get(request.getElementIndex());

        // Apply decorators based on requested styles
        Element styledElement = applyDecorators(element, request.getStyles());

        // Replace the element with the styled version
        document.getElements().set(request.getElementIndex(), styledElement);
        documentService.saveDocument(document);

        return ResponseEntity.ok(
                ApiResponse.success("Styles applied successfully", document));
    }

    /**
     * Applies multiple decorators to an element.
     * Demonstrates the Decorator pattern's ability to stack behaviors.
     * 
     * @param element base element
     * @param styles  list of styles to apply
     * @return decorated element
     */
    private Element applyDecorators(Element element, java.util.List<String> styles) {
        Element result = element;

        for (String style : styles) {
            result = switch (style.toLowerCase()) {
                case "bold" -> new BoldDecorator(result);
                case "italic" -> new ItalicDecorator(result);
                default -> {
                    if (style.startsWith("color:")) {
                        String color = style.substring(6);
                        yield new ColorDecorator(result, color);
                    } else if (style.startsWith("size:")) {
                        String size = style.substring(5);
                        yield new SizeDecorator(result, size);
                    }
                    yield result;
                }
            };
        }

        return result;
    }
}
