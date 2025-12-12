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
    public void export(Document document, java.io.OutputStream out) {
        StringBuilder markdown = new StringBuilder();

        // Title
        markdown.append("# ").append(document.getTitle()).append("\n\n");

        if (document.getMetadata() != null && !document.getMetadata().isEmpty()) {
            markdown.append("> Metadata: ").append(document.getMetadata()).append("\n\n");
        }

        for (Element element : document.getElements()) {
            markdown.append(renderElement(element)).append("\n\n");
        }

        try {
            out.write(markdown.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));
        } catch (java.io.IOException e) {
            throw new RuntimeException("Error writing Markdown to output stream", e);
        }
    }

    private String renderElement(Element element) {
        // Simple rendering based on type
        // In a real app we would use polymorphic rendering
        return element.render();
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
