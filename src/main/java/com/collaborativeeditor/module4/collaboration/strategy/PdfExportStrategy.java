package com.collaborativeeditor.module4.collaboration.strategy;

import com.collaborativeeditor.module1.creation.model.Document;
import com.collaborativeeditor.module1.creation.model.Element;
import org.springframework.stereotype.Component;

/**
 * Concrete strategy for exporting documents as PDF (simulated).
 * Part of the Strategy pattern.
 * 
 * In a real implementation, this would use a library like iText or Apache
 * PDFBox.
 * For this demo, it returns a text representation.
 * 
 * @author Arch_Force Team
 */
@Component
public class PdfExportStrategy implements ExportStrategy {

    @Override
    public String export(Document document) {
        StringBuilder pdf = new StringBuilder();

        pdf.append("=".repeat(60)).append("\n");
        pdf.append("PDF EXPORT - ").append(document.getTitle()).append("\n");
        pdf.append("=".repeat(60)).append("\n\n");

        pdf.append("Author: ").append(document.getAuthor()).append("\n");
        pdf.append("Created: ").append(document.getCreatedAt()).append("\n");
        pdf.append("Last Modified: ").append(document.getLastModified()).append("\n\n");

        if (document.getMetadata() != null && !document.getMetadata().isEmpty()) {
            pdf.append("Metadata: ").append(document.getMetadata()).append("\n\n");
        }

        pdf.append("-".repeat(60)).append("\n");
        pdf.append("CONTENT\n");
        pdf.append("-".repeat(60)).append("\n\n");

        for (int i = 0; i < document.getElements().size(); i++) {
            Element element = document.getElements().get(i);
            pdf.append("[").append(i + 1).append("] ")
                    .append(element.getType().toUpperCase())
                    .append(": ")
                    .append(element.getContent())
                    .append("\n\n");
        }

        pdf.append("=".repeat(60)).append("\n");
        pdf.append("END OF DOCUMENT\n");
        pdf.append("=".repeat(60)).append("\n");

        return pdf.toString();
    }

    @Override
    public String getFormatName() {
        return "PDF";
    }

    @Override
    public String getFileExtension() {
        return "pdf";
    }
}
