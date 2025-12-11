package com.collaborativeeditor.module1.creation.factory;

import com.collaborativeeditor.module1.creation.model.Element;

/**
 * Factory Method pattern for creating different types of document elements.
 * Defines the interface for creating Element objects.
 * 
 * This follows the Factory Method pattern, allowing subclasses to decide
 * which concrete Element class to instantiate.
 * 
 * @author Arch_Force Team
 */
public interface ElementFactory {

    /**
     * Factory method to create an element.
     * 
     * @return created Element instance
     */
    Element createElement();

    /**
     * Gets the type of element this factory creates.
     * 
     * @return element type
     */
    String getElementType();
}
