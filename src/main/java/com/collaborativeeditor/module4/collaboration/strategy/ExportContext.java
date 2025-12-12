package com.collaborativeeditor.module4.collaboration.strategy;

import com.collaborativeeditor.module1.creation.model.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * Context class for the Strategy pattern.
 * Allows switching between different export strategies at runtime.
 * 
 * @author Arch_Force Team
 */
@Component
@RequiredArgsConstructor
public class ExportContext {

    private final HtmlExportStrategy htmlExportStrategy;
    private final MarkdownExportStrategy markdownExportStrategy;
    private final PdfExportStrategy pdfExportStrategy;

    private ExportStrategy currentStrategy;

    /**
     * Sets the export strategy to use.
     * 
     * @param strategy export strategy
     */
    public void setStrategy(ExportStrategy strategy) {
        this.currentStrategy = strategy;
    }

    /**
     * Sets the export strategy by format name.
     * 
     * @param format format name (html, markdown, pdf)
     */
    public void setStrategyByFormat(String format) {
        this.currentStrategy = switch (format.toLowerCase()) {
            case "html" -> htmlExportStrategy;
            case "markdown", "md" -> markdownExportStrategy;
            case "pdf" -> pdfExportStrategy;
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        };
    }

    /**
     * Exports a document using the current strategy.
     * 
     * @param document document to export
     * @return exported content
     */
    public byte[] executeExport(Document document) {
        if (currentStrategy == null) {
            throw new IllegalStateException("Export strategy not set");
        }
        try (java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream()) {
            currentStrategy.export(document, out);
            return out.toByteArray();
        } catch (java.io.IOException e) {
            throw new RuntimeException("Error exporting document", e);
        }
    }

    /**
     * Gets all available export formats.
     * 
     * @return map of format names to strategies
     */
    public Map<String, ExportStrategy> getAvailableFormats() {
        Map<String, ExportStrategy> formats = new HashMap<>();
        formats.put("html", htmlExportStrategy);
        formats.put("markdown", markdownExportStrategy);
        formats.put("pdf", pdfExportStrategy);
        return formats;
    }
}
