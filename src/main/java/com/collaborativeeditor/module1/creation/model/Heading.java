package com.collaborativeeditor.module1.creation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a heading element in the document.
 * 
 * @author Arch_Force Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@DiscriminatorValue("heading")
@EqualsAndHashCode(callSuper = true)
public class Heading extends Element {
    private String content;
    private int level; // 1 to 6

    @Override
    public String getType() {
        return "heading";
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String render() {
        return String.format("<h%d>%s</h%d>", level, content, level);
    }
}
