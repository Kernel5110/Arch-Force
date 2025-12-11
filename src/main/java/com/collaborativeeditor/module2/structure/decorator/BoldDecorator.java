package com.collaborativeeditor.module2.structure.decorator;

import com.collaborativeeditor.module1.creation.model.Element;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

/**
 * Decorator for adding bold style.
 * 
 * @author Arch_Force Team
 */
@Entity
@DiscriminatorValue("bold")
@NoArgsConstructor
public class BoldDecorator extends StyleDecorator {

    public BoldDecorator(Element element) {
        super(element);
    }

    @Override
    protected String getStyleTag() {
        return "strong";
    }

    @Override
    protected String getInlineStyle() {
        return "font-weight: bold;";
    }

    @Override
    public String render() {
        return String.format("<%s style=\"%s\">%s</%s>",
                getStyleTag(), getInlineStyle(), wrappedElement.render(), getStyleTag());
    }
}
