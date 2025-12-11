package com.collaborativeeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

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

    @NotBlank(message = "Element type is required")
    private String elementType;

    private Map<String, Object> elementData;
}
