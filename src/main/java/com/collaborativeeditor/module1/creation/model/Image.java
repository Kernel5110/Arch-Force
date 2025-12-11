package com.collaborativeeditor.module1.creation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents an image element.
 * 
 * @author Arch_Force Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@DiscriminatorValue("image")
@EqualsAndHashCode(callSuper = true)
public class Image extends Element {

    @Column(columnDefinition = "TEXT")
    private String url;
    private String altText;

    @Override
    public String getType() {
        return "image";
    }

    @Override
    public String getContent() {
        return url; // Primary content is URL
    }

    @Override
    public String render() {
        return "<img src=\"" + url + "\" alt=\"" + (altText != null ? altText : "") + "\" />";
    }
}
