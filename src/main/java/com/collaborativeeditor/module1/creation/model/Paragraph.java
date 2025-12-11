package com.collaborativeeditor.module1.creation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Concrete implementation of Element representing a paragraph.
 * Part of the Factory Method pattern.
 * 
 * @author Arch_Force Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@DiscriminatorValue("paragraph")
@EqualsAndHashCode(callSuper = true)
public class Paragraph extends Element {

    @Column(columnDefinition = "TEXT")
    private String content;

    @Override
    public String getType() {
        return "paragraph";
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String render() {
        return "<p>" + content + "</p>";
    }
}
