package com.collaborativeeditor.module2.structure.composite;

import java.util.List;

/**
 * Component interface for the Composite pattern.
 * Represents both individual elements and composite structures.
 * 
 * This interface allows treating individual objects and compositions
 * of objects uniformly (Composite pattern).
 * 
 * @author Arch_Force Team
 */
public interface DocumentComponent {

    /**
     * Gets the name of this component.
     * 
     * @return component name
     */
    String getName();

    /**
     * Gets the type of this component.
     * 
     * @return component type
     */
    String getType();

    /**
     * Renders this component and all its children.
     * 
     * @return rendered output
     */
    String render();

    /**
     * Adds a child component (for composite nodes).
     * 
     * @param component child to add
     */
    void add(DocumentComponent component);

    /**
     * Removes a child component (for composite nodes).
     * 
     * @param component child to remove
     */
    void remove(DocumentComponent component);

    /**
     * Gets all children of this component.
     * 
     * @return list of children
     */
    List<DocumentComponent> getChildren();

    /**
     * Gets the depth level of this component in the hierarchy.
     * 
     * @return depth level
     */
    int getLevel();

    /**
     * Sets the depth level of this component.
     * 
     * @param level depth level
     */
    void setLevel(int level);
}
