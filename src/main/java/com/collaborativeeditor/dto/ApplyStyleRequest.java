package com.collaborativeeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * DTO for applying styles to elements.
 * Used for POST /api/styles/apply endpoint.
 * 
 * @author Arch_Force Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyStyleRequest {

    @NotBlank(message = "Document ID is required")
    private String documentId;

    private int elementIndex;

    @NotBlank(message = "At least one style must be specified")
    private List<String> styles; // e.g., ["bold", "italic", "color:red", "size:20px"]
}
