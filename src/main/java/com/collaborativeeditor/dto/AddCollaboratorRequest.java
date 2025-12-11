package com.collaborativeeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for adding a collaborator to a document.
 * Used for POST /api/collaborators/add endpoint.
 * 
 * @author Arch_Force Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCollaboratorRequest {

    @NotBlank(message = "Document ID is required")
    private String documentId;

    @NotBlank(message = "Collaborator name is required")
    private String collaboratorName;

    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;
}
