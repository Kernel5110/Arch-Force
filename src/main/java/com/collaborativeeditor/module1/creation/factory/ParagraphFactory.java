package com.collaborativeeditor.module1.creation.factory;

import com.collaborativeeditor.module1.creation.model.Element;
import com.collaborativeeditor.module1.creation.model.Paragraph;
import org.springframework.stereotype.Component;

/**
 * Concrete factory for creating Paragraph elements.
 * Implements the Factory Method pattern.
 * 
 * @author Arch_Force Team
 */
@Component
public class ParagraphFactory implements ElementFactory {

    private String content;

    public ParagraphFactory() {
        this.content = "";
    }

    public ParagraphFactory withContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public Element createElement() {
        return new Paragraph(content);
    }

    @Override
    public String getElementType() {
        return "paragraph";
    }
}
