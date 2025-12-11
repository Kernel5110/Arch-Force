package com.collaborativeeditor.module3.versioning.command;

import com.collaborativeeditor.module1.creation.model.Document;
import com.collaborativeeditor.module1.creation.model.Element;
import lombok.Getter;

/**
 * Concrete command for adding an element to a document.
 * Implements the Command pattern.
 * 
 * @author Arch_Force Team
 */
@Getter
public class AddElementCommand implements Command {

    private final Document document;
    private final Element element;
    private final String description;

    public AddElementCommand(Document document, Element element) {
        this.document = document;
        this.element = element;
        this.description = "Add " + element.getType() + " element";
    }

    @Override
    public void execute() {
        document.addElement(element);
    }

    @Override
    public void undo() {
        if (!document.getElements().isEmpty()) {
            document.getElements().remove(document.getElements().size() - 1);
        }
    }
}
