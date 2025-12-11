package com.collaborativeeditor.module1.creation.factory;

import com.collaborativeeditor.module1.creation.model.Element;
import com.collaborativeeditor.module1.creation.model.Heading;

/**
 * Factory for creating Heading elements.
 * 
 * @author Arch_Force Team
 */
public class HeadingFactory implements ElementFactory {

    private String content;
    private int level = 1;

    public HeadingFactory withContent(String content) {
        this.content = content;
        return this;
    }

    public HeadingFactory withLevel(int level) {
        if (level < 1)
            level = 1;
        if (level > 6)
            level = 6;
        this.level = level;
        return this;
    }

    @Override
    public Element createElement() {
        return Heading.builder()
                .content(content)
                .level(level)
                .build();
    }

    @Override
    public String getElementType() {
        return "heading";
    }
}
