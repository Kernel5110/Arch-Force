package com.collaborativeeditor.module4.collaboration.strategy;

import com.collaborativeeditor.module1.creation.model.Document;
import com.collaborativeeditor.module1.creation.model.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

/**
 * Concrete strategy for exporting documents as PDF using OpenPDF.
 * Part of the Strategy pattern.
 * 
 * @author Arch_Force Team
 */
@Component
public class PdfExportStrategy implements ExportStrategy {

    @Override
    public byte[] export(Document document) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            com.lowagie.text.Document pdfDoc = new com.lowagie.text.Document();
            PdfWriter.getInstance(pdfDoc, out);

            pdfDoc.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph(document.getTitle(), titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            title.setSpacingAfter(20);
            pdfDoc.add(title);

            // Metadata
            Font metaFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10);
            pdfDoc.add(new Paragraph("Author: " + document.getAuthor(), metaFont));
            pdfDoc.add(new Paragraph("Created: " + document.getCreatedAt(), metaFont));

            if (document.getMetadata() != null && !document.getMetadata().isEmpty()) {
                pdfDoc.add(new Paragraph("Metadata: " + document.getMetadata(), metaFont));
            }

            pdfDoc.add(new Paragraph("\n")); // Spacer

            // Content
            for (Element element : document.getElements()) {
                pdfDoc.add(renderElementToPdf(element));
                pdfDoc.add(new Paragraph("\n")); // Spacer
            }

            pdfDoc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    private Paragraph renderElementToPdf(Element element) {
        // Basic mapping. In a real app we'd need more complex logic/visitor
        String type = element.getType().toLowerCase();

        Font font = FontFactory.getFont(FontFactory.HELVETICA, 12);

        switch (type) {
            case "heading":
                font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
                break;
            case "image":
                return new Paragraph("[Image: " + element.getContent() + "]", font);
            // Add other types as needed
        }

        return new Paragraph(element.getContent(), font);
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
