package com.perfexiolegal.billingsystem.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.perfexiolegal.billingsystem.Model.ApiResponse;

@RestController
@RequestMapping("/api/pdf-upload")
public class PdfUploadController {
    private final Logger logger = LoggerFactory.getLogger(PdfUploadController.class);

    @PostMapping(value="/invoice", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) 
    public ResponseEntity<ApiResponse> uploadInvoice(@RequestParam("pdf") MultipartFile file, @RequestParam("directory") String directory) throws IOException {
        logger.info("Start invoice upload: {}", file.getOriginalFilename());
        if (file.isEmpty()) {
            logger.error("No file provided.");
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message("No file provided.")
                    .success(false)
                    .build());
        }
    
        Path targetPath = Path.of("/Users/snehil1998/Documents/Invoices/", directory);
        logger.info("Uploading invoice {} PDF for disbursement: {}", file.getOriginalFilename(), targetPath);

        try {
            Files.createDirectories(targetPath.getParent());
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File uploaded successfully: {}", file.getOriginalFilename());
        } catch (IOException e) {
            logger.error("Failed to upload file: " + e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message("Failed to upload file.")
                    .success(false)
                    .build());
        }
    
        return ResponseEntity.ok(ApiResponse.builder()
                .message("File uploaded successfully: " + file.getOriginalFilename())
                .success(true)
                .build());
    }
}
