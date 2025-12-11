package com.collaborativeeditor.module4.collaboration.strategy;

import com.collaborativeeditor.module1.creation.model.Document;
import org.springframework.stereotype.Component;

/**
 * Concrete strategy for exporting documents as HTML.
 * Part of the Strategy pattern.
 * 
 * @author Arch_Force Team
 */
@Component
public class HtmlExportStrategy implements ExportStrategy {

    @Override
    public byte[] export(Document document) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h1>").append(document.getTitle()).append("</h1>");
        html.append("<p><em>By ").append(document.getAuthor()).append("</em></p>");

        for (com.collaborativeeditor.module1.creation.model.Element element : document.getElements()) {
            html.append(element.render());
        }

        html.append("</body></html>");
        return html.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    @Override
    public String getFormatName() {
        return "HTML";
    }

    @Override
    public String getFileExtension() {
        return "html";
    }
}
