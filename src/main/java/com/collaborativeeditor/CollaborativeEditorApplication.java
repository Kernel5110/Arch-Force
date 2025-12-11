package com.collaborativeeditor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application for Collaborative Document Editor.
 * Implements 8 GoF Design Patterns across 4 functional modules.
 * 
 * @author Arch_Force Team
 * @version 1.0.0
 */
@SpringBootApplication
public class CollaborativeEditorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollaborativeEditorApplication.class, args);
    }
}
