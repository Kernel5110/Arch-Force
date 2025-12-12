package com.collaborativeeditor.module1.creation.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

import lombok.Data;

/**
 * Base interface for all document elements.
 * Part of the Factory Method pattern.
 * 
 * @author Arch_Force Team
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Paragraph.class, name = "paragraph"),
        @JsonSubTypes.Type(value = Image.class, name = "image"),
        @JsonSubTypes.Type(value = Table.class, name = "table"),
        @JsonSubTypes.Type(value = ListElement.class, name = "list"),
        @JsonSubTypes.Type(value = Heading.class, name = "heading"),
        @JsonSubTypes.Type(value = CodeBlock.class, name = "code")
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "element_type")
@jakarta.persistence.Table(name = "elements")
@Data
public abstract class Element {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Gets the type of this element.
     * 
     * @return element type
     */
    public abstract String getType();

    /**
     * Gets the content of this element.
     * 
     * @return element content
     */
    public abstract String getContent();

    /**
     * Renders this element as a string.
     * 
     * @return rendered element
     */
    public abstract String render();
}
