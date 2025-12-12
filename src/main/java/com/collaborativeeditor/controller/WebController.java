package com.collaborativeeditor.controller;

import com.collaborativeeditor.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final DocumentService documentService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("documents", documentService.getAllDocuments());
        return "index";
    }

    @GetMapping("/editor/{id}")
    public String editor(@PathVariable String id, Model model) {
        model.addAttribute("documentId", id);
        // We can also fetch the document here to check existence, but JS fetch is fine
        // for details
        return "editor";
    }

    @GetMapping("/recycle-bin")
    public String recycleBin(Model model) {
        model.addAttribute("documents", documentService.getRecycleBinDocuments());
        return "recycle_bin"; // Thymeleaf template
    }
}
