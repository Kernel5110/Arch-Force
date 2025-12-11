package com.collaborativeeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for exporting a document.
 * Used for POST /api/export/document endpoint.
 * 
 * @author Arch_Force Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportDocumentRequest {

    @NotBlank(message = "Document ID is required")
    private String documentId;

    @NotBlank(message = "Format is required")
    private String format; // html, markdown, pdf
}
