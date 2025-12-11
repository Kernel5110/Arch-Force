package com.collaborativeeditor.module3.versioning.command;

import com.collaborativeeditor.module1.creation.model.Document;
import com.collaborativeeditor.module1.creation.model.Element;
import lombok.Getter;

/**
 * Concrete command for removing an element from a document.
 * Implements the Command pattern.
 * 
 * @author Arch_Force Team
 */
@Getter
public class RemoveElementCommand implements Command {

    private final Document document;
    private final int index;
    private Element removedElement;
    private final String description;

    public RemoveElementCommand(Document document, int index) {
        this.document = document;
        this.index = index;
        this.description = "Remove element at index " + index;
    }

    @Override
    public void execute() {
        if (index >= 0 && index < document.getElements().size()) {
            removedElement = document.getElements().remove(index);
        }
    }

    @Override
    public void undo() {
        if (removedElement != null && index >= 0 && index <= document.getElements().size()) {
            document.getElements().add(index, removedElement);
        }
    }
}
