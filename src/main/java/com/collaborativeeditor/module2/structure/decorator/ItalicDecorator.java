package com.collaborativeeditor.module2.structure.decorator;

import com.collaborativeeditor.module1.creation.model.Element;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

/**
 * Concrete decorator for applying italic style.
 * Part of the Decorator pattern.
 * 
 * @author Arch_Force Team
 */
@Entity
@DiscriminatorValue("italic")
@NoArgsConstructor
public class ItalicDecorator extends StyleDecorator {

    public ItalicDecorator(Element element) {
        super(element);
    }

    @Override
    protected String getStyleTag() {
        return "em";
    }

    @Override
    protected String getInlineStyle() {
        return "font-style: italic;";
    }

    @Override
    public String render() {
        return String.format("<%s style=\"%s\">%s</%s>",
                getStyleTag(), getInlineStyle(), wrappedElement.render(), getStyleTag());
    }
}
