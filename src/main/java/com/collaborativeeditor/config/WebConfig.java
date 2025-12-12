package com.collaborativeeditor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve files from the 'uploads' directory at the root of the project
        // Access via: http://localhost:8085/uploads/filename.ext
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
