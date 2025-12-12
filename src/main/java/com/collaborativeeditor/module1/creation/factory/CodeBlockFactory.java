package com.collaborativeeditor.module1.creation.factory;

import com.collaborativeeditor.module1.creation.model.CodeBlock;
import com.collaborativeeditor.module1.creation.model.Element;
import org.springframework.stereotype.Component;

/**
 * Concrete factory for creating CodeBlock elements.
 * 
 * @author Arch_Force Team
 */
@Component
public class CodeBlockFactory implements ElementFactory {

    private String content;
    private String language;

    public CodeBlockFactory() {
        this.content = "";
        this.language = "plaintext";
    }

    public CodeBlockFactory withContent(String content) {
        this.content = content;
        return this;
    }

    public CodeBlockFactory withLanguage(String language) {
        this.language = language;
        return this;
    }

    @Override
    public Element createElement() {
        return new CodeBlock(content, language);
    }

    @Override
    public String getElementType() {
        return "code";
    }
}
