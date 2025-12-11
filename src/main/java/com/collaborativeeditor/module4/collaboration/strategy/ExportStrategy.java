package com.collaborativeeditor.module4.collaboration.strategy;

import com.collaborativeeditor.module1.creation.model.Document;

/**
 * Strategy interface for the Strategy pattern.
 * Defines the contract for different export formats.
 * 
 * @author Arch_Force Team
 */
public interface ExportStrategy {

    /**
     * Exports a document in a specific format.
     * 
     * @param document document to export
     * @return exported content as bytes
     */
    byte[] export(Document document);

    /**
     * Gets the format name of this export strategy.
     * 
     * @return format name
     */
    String getFormatName();

    /**
     * Gets the file extension for this export format.
     * 
     * @return file extension (e.g., "pdf", "html", "md")
     */
    String getFileExtension();
}
