package com.example.mobile_store.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.example.mobile_store.constant.ApiUrlConstant;
import com.example.mobile_store.exception.StorageFileNotFoundException;
import com.example.mobile_store.service.StorageService;

@RestController
@RequestMapping(ApiUrlConstant.UPLOAD)
@Controller
public class FileUploadController {
    private final StorageService storageService;

    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping
    public String listUploadedFiles(Model model) throws IOException {
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType;
        if (filename.endsWith(".png")) {
            contentType = MediaType.IMAGE_PNG_VALUE;
        } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            contentType = MediaType.IMAGE_JPEG_VALUE;
        } else {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Loại khác
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }

    @PostMapping
    public ResponseEntity<List<String>> handleFileUpload(@RequestParam("file") List<MultipartFile> files) {
        List<String> fileDetails = new ArrayList<>();
        for (MultipartFile file : files) {
            String filename = storageService.store(file);
            String fileUrl = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                    "serveFile", filename).build().toUri().toString();
            fileDetails.add("Name: " + filename);
            fileDetails.add(" URL: " + fileUrl);
        }
        return ResponseEntity.ok().body(fileDetails);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteAllFiles() {
        storageService.deleteAll();
        return ResponseEntity.ok().body("All files deleted successfully");
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
