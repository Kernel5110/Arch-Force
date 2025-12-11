package com.collaborativeeditor.module1.creation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a document in the system.
 * 
 * @author Arch_Force Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@jakarta.persistence.Table(name = "documents")
public class Document {

    @Id
    @Column(length = 36)
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "document_id")
    @Builder.Default
    private List<Element> elements = new ArrayList<>();

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime lastModified = LocalDateTime.now();

    @Version
    private Long version; // Optimistic locking

    /**
     * Adds an element to the document.
     * 
     * @param element element to add
     */
    public void addElement(Element element) {
        this.elements.add(element);
        this.lastModified = LocalDateTime.now();
    }

    /**
     * Renders the entire document as HTML.
     * 
     * @return HTML representation
     */
    public String renderAsHtml() {
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h1>").append(title).append("</h1>");
        html.append("<p><em>By ").append(author).append("</em></p>");

        for (Element element : elements) {
            html.append(element.render());
        }

        html.append("</body></html>");
        return html.toString();
    }
}
