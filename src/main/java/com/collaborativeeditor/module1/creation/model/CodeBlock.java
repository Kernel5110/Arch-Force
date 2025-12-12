package com.collaborativeeditor.module1.creation.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Concrete implementation of Element representing a code block.
 * 
 * @author Arch_Force Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@DiscriminatorValue("code")
@EqualsAndHashCode(callSuper = true)
public class CodeBlock extends Element {

    @Column(columnDefinition = "TEXT")
    private String content;

    private String language;

    @Override
    public String getType() {
        return "code";
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String render() {
        return "<pre><code class=\"language-" + (language != null ? language : "plaintext") + "\">"
                + content + "</code></pre>";
    }
}
