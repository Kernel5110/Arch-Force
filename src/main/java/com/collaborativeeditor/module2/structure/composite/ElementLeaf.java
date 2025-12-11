package com.collaborativeeditor.module2.structure.composite;

import com.collaborativeeditor.module1.creation.model.Element;
import lombok.Data;
import java.util.Collections;
import java.util.List;

/**
 * Leaf node in the Composite pattern.
 * Wraps an Element and cannot have children.
 * 
 * @author Arch_Force Team
 */
@Data
public class ElementLeaf implements DocumentComponent {

    private Element element;
    private int level;

    public ElementLeaf(Element element) {
        this.element = element;
        this.level = 0;
    }

    @Override
    public String getName() {
        return element.getType() + ": " + element.getContent();
    }

    @Override
    public String getType() {
        return element.getType();
    }

    @Override
    public String render() {
        return element.render();
    }

    @Override
    public void add(DocumentComponent component) {
        throw new UnsupportedOperationException("Cannot add children to a leaf element");
    }

    @Override
    public void remove(DocumentComponent component) {
        throw new UnsupportedOperationException("Cannot remove children from a leaf element");
    }

    @Override
    public List<DocumentComponent> getChildren() {
        return Collections.emptyList();
    }
}
