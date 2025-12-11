package com.collaborativeeditor.module3.versioning.memento;

import com.collaborativeeditor.module1.creation.model.Element;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Memento class in the Memento pattern.
 * Stores a snapshot of a document's state for later restoration.
 * 
 * @author Arch_Force Team
 */
@Getter
public class DocumentMemento {

    private final String id;
    private final String title;
    private final String author;
    private final List<Element> elements;
    private final String metadata;
    private final LocalDateTime snapshotTime;
    private final String version;

    public DocumentMemento(String id, String title, String author,
            List<Element> elements, String metadata, String version) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.elements = new ArrayList<>(elements); // Deep copy
        this.metadata = metadata;
        this.snapshotTime = LocalDateTime.now();
        this.version = version;
    }
}
