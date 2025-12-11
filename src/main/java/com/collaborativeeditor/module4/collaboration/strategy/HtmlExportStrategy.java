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
    public String export(Document document) {
        return document.renderAsHtml();
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
