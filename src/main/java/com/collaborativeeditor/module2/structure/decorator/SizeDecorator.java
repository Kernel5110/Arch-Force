package com.collaborativeeditor.module2.structure.decorator;

import com.collaborativeeditor.module1.creation.model.Element;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Concrete decorator for applying font size style.
 * Part of the Decorator pattern.
 * 
 * @author Arch_Force Team
 */
@Entity
@DiscriminatorValue("size")
@NoArgsConstructor
@Getter
@Setter
public class SizeDecorator extends StyleDecorator {

    private String size;

    public SizeDecorator(Element element, String size) {
        super(element);
        this.size = size;
    }

    @Override
    protected String getStyleTag() {
        return "span";
    }

    @Override
    protected String getInlineStyle() {
        return "font-size: " + size + ";";
    }

    @Override
    public String render() {
        return "<" + getStyleTag() + " style=\"" + getInlineStyle() + "\">"
                + wrappedElement.render()
                + "</" + getStyleTag() + ">";
    }
}
