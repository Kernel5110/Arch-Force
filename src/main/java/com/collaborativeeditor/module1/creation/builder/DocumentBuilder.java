package com.collaborativeeditor.module1.creation.builder;

import com.collaborativeeditor.module1.creation.model.Document;
import com.collaborativeeditor.module1.creation.model.Element;
import org.springframework.stereotype.Component;
import java.util.UUID;
import java.time.LocalDateTime;

/**
 * Builder pattern implementation for constructing complex Document objects.
 * Allows step-by-step construction of documents with a fluent interface.
 * 
 * This follows the Builder pattern, separating the construction of a complex
 * object from its representation.
 * 
 * @author Arch_Force Team
 */
@Component
public class DocumentBuilder {

    private Document document;

    public DocumentBuilder() {
        reset();
    }

    /**
     * Resets the builder to create a new document.
     * 
     * @return this builder
     */
    public DocumentBuilder reset() {
        this.document = new Document();
        this.document.setId(UUID.randomUUID().toString());
        return this;
    }

    /**
     * Sets the title of the document.
     * 
     * @param title document title
     * @return this builder
     */
    public DocumentBuilder withTitle(String title) {
        this.document.setTitle(title);
        return this;
    }

    /**
     * Sets the author of the document.
     * 
     * @param author document author
     * @return this builder
     */
    public DocumentBuilder withAuthor(String author) {
        this.document.setAuthor(author);
        return this;
    }

    /**
     * Adds an element to the document.
     * 
     * @param element element to add
     * @return this builder
     */
    public DocumentBuilder addElement(Element element) {
        this.document.addElement(element);
        return this;
    }

    /**
     * Sets metadata for the document.
     * 
     * @param metadata document metadata
     * @return this builder
     */
    public DocumentBuilder withMetadata(String metadata) {
        this.document.setMetadata(metadata);
        return this;
    }

    /**
     * Builds and returns the final document.
     * The builder is reset after building.
     * 
     * @return constructed document
     */
    public Document build() {
        Document result = this.document;
        reset();
        return result;
    }

    /**
     * Gets the current document being built (without resetting).
     * 
     * @return current document
     */
    public Document getResult() {
        return this.document;
    }
}
