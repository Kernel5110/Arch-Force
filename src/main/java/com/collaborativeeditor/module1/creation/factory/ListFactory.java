package com.collaborativeeditor.module1.creation.factory;

import com.collaborativeeditor.module1.creation.model.Element;
import com.collaborativeeditor.module1.creation.model.ListElement;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Concrete factory for creating List elements.
 * Implements the Factory Method pattern.
 * 
 * @author Arch_Force Team
 */
@Component
public class ListFactory implements ElementFactory {

    private List<String> items;
    private boolean ordered;

    public ListFactory() {
        this.items = List.of();
        this.ordered = false;
    }

    public ListFactory withItems(List<String> items) {
        this.items = items;
        return this;
    }

    public ListFactory ordered(boolean ordered) {
        this.ordered = ordered;
        return this;
    }

    @Override
    public Element createElement() {
        return new ListElement(items, ordered);
    }

    @Override
    public String getElementType() {
        return "list";
    }
}
