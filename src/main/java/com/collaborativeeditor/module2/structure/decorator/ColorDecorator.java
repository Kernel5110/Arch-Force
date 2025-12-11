package com.collaborativeeditor.module2.structure.decorator;

import com.collaborativeeditor.module1.creation.model.Element;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Concrete decorator for applying color style.
 * Part of the Decorator pattern.
 * 
 * @author Arch_Force Team
 */
@Entity
@DiscriminatorValue("color")
@NoArgsConstructor
@Getter
@Setter
public class ColorDecorator extends StyleDecorator {

    private String color;

    public ColorDecorator(Element element, String color) {
        super(element);
        this.color = color;
    }

    @Override
    protected String getStyleTag() {
        return "span";
    }

    @Override
    protected String getInlineStyle() {
        return "color: " + color + ";";
    }

    @Override
    public String render() {
        return "<" + getStyleTag() + " style=\"" + getInlineStyle() + "\">"
                + wrappedElement.render()
                + "</" + getStyleTag() + ">";
    }
}
