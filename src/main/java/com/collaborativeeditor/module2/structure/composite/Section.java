package com.collaborativeeditor.module2.structure.composite;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Composite node in the Composite pattern.
 * Represents a container that can hold other components (sections,
 * subsections).
 * 
 * @author Arch_Force Team
 */
@Data
public class Section implements DocumentComponent {

    private String name;
    private int level;
    private List<DocumentComponent> children;

    public Section(String name) {
        this.name = name;
        this.children = new ArrayList<>();
        this.level = 0;
    }

    @Override
    public String getType() {
        return "section";
    }

    @Override
    public void add(DocumentComponent component) {
        component.setLevel(this.level + 1);
        this.children.add(component);
    }

    @Override
    public void remove(DocumentComponent component) {
        this.children.remove(component);
    }

    @Override
    public List<DocumentComponent> getChildren() {
        return new ArrayList<>(children);
    }

    @Override
    public String render() {
        StringBuilder html = new StringBuilder();

        // Render section heading
        int headingLevel = Math.min(level + 1, 6); // H1-H6
        html.append("<h").append(headingLevel).append(">")
                .append(name)
                .append("</h").append(headingLevel).append(">\n");

        // Render all children
        html.append("<div class=\"section\" data-level=\"").append(level).append("\">\n");
        for (DocumentComponent child : children) {
            html.append(child.render()).append("\n");
        }
        html.append("</div>\n");

        return html.toString();
    }
}
