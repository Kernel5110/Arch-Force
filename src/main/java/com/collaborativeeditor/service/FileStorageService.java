package com.collaborativeeditor.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("uploads");

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file " + filename);
        }
        if (filename.contains("..")) {
            throw new RuntimeException("Cannot store file with relative path outside current directory " + filename);
        }

        try {
            String extension = "";
            int i = filename.lastIndexOf('.');
            if (i >= 0) {
                extension = filename.substring(i);
            }

            String newFilename = UUID.randomUUID().toString() + extension;

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(newFilename),
                        StandardCopyOption.REPLACE_EXISTING);
            }

            return "/uploads/" + newFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + filename, e);
        }
    }
}
