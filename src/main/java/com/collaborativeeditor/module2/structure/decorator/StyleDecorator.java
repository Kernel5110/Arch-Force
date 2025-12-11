package com.collaborativeeditor.module2.structure.decorator;

import com.collaborativeeditor.module1.creation.model.Element;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Base Decorator for applying styles to elements.
 * Implements the Decorator pattern for adding functionality dynamically.
 * 
 * @author Arch_Force Team
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public abstract class StyleDecorator extends Element {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "wrapped_element_id")
    protected Element wrappedElement;

    public StyleDecorator(Element element) {
        this.wrappedElement = element;
    }

    @Override
    public String getType() {
        return wrappedElement != null ? wrappedElement.getType() : "decorator";
    }

    @Override
    public String getContent() {
        return wrappedElement != null ? wrappedElement.getContent() : "";
    }

    /**
     * Gets the style tag name for this decorator.
     * 
     * @return style tag
     */
    protected abstract String getStyleTag();

    /**
     * Gets any inline CSS styles for this decorator.
     * 
     * @return CSS style string
     */
    protected abstract String getInlineStyle();
}
