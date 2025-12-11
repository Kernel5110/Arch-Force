package com.collaborativeeditor.module1.creation.factory;

import com.collaborativeeditor.module1.creation.model.Element;
import com.collaborativeeditor.module1.creation.model.Image;
import org.springframework.stereotype.Component;

/**
 * Concrete factory for creating Image elements.
 * Implements the Factory Method pattern.
 * 
 * @author Arch_Force Team
 */
@Component
public class ImageFactory implements ElementFactory {

    private String url;
    private String altText;

    public ImageFactory() {
        this.url = "";
        this.altText = "";
    }

    public ImageFactory withUrl(String url) {
        this.url = url;
        return this;
    }

    public ImageFactory withAltText(String altText) {
        this.altText = altText;
        return this;
    }

    @Override
    public Element createElement() {
        return new Image(url, altText);
    }

    @Override
    public String getElementType() {
        return "image";
    }
}
