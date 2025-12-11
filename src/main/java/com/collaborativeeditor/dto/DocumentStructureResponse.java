package com.collaborativeeditor.dto;

import com.collaborativeeditor.module2.structure.composite.DocumentComponent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for document structure representation.
 * Used for GET /api/documents/structure endpoint.
 * 
 * @author Arch_Force Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentStructureResponse {

    private String documentId;
    private String title;
    private DocumentComponent root;
}
