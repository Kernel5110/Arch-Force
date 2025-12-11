package com.collaborativeeditor.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * Request DTO for updating an existing element in a document.
 * 
 * @author Arch_Force Team
 */
@Data
public class UpdateElementRequest {

    @NotEmpty(message = "Document ID is required")
    private String documentId;

    @NotNull(message = "Element ID is required")
    private Long elementId;

    @NotNull(message = "Element data is required")
    private Map<String, Object> elementData;
}
