package com.collaborativeeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * DTO for adding an element to a document.
 * Used for POST /api/documents/add-element endpoint.
 * 
 * @author Arch_Force Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddElementRequest {

    @NotBlank(message = "Document ID is required")
    private String documentId;

    private String elementType;

    private java.util.Map<String, Object> elementData;
}
