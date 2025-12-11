package com.collaborativeeditor.module4.collaboration.strategy;

import com.collaborativeeditor.module1.creation.model.Document;
import com.collaborativeeditor.module1.creation.model.Element;
import org.springframework.stereotype.Component;

/**
 * Concrete strategy for exporting documents as Markdown.
 * Part of the Strategy pattern.
 * 
 * @author Arch_Force Team
 */
@Component
public class MarkdownExportStrategy implements ExportStrategy {

    @Override
    public String export(Document document) {
        StringBuilder markdown = new StringBuilder();

        // Title
        markdown.append("# ").append(document.getTitle()).append("\n\n");

        // Author
        markdown.append("**Author:** ").append(document.getAuthor()).append("\n\n");

        // Metadata
        if (document.getMetadata() != null && !document.getMetadata().isEmpty()) {
            markdown.append("**Metadata:** ").append(document.getMetadata()).append("\n\n");
        }

        // Elements
        for (Element element : document.getElements()) {
            markdown.append(convertElementToMarkdown(element)).append("\n\n");
        }

        return markdown.toString();
    }

    private String convertElementToMarkdown(Element element) {
        return switch (element.getType()) {
            case "paragraph" -> element.getContent();
            case "image" -> "![Image](" + element.getContent() + ")";
            case "table" -> "| Table | (table content omitted in markdown) |";
            case "list" -> "- " + element.getContent();
            default -> element.getContent();
        };
    }

    @Override
    public String getFormatName() {
        return "Markdown";
    }

    @Override
    public String getFileExtension() {
        return "md";
    }
}
