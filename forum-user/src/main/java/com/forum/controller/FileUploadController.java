// FileUploadController.java
package com.forum.controller;

import com.forum.service.FileUploadService;
import com.forum.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/image")
    public Result<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, String> result = fileUploadService.uploadFile(file, "image");
        return Result.success(result);
    }

    @PostMapping("/payment-proof")
    public Result<Map<String, String>> uploadPaymentProof(@RequestParam("file") MultipartFile file) {
        Map<String, String> result = fileUploadService.uploadFile(file, "payment");
        return Result.success(result);
    }

    @PostMapping("/document")
    public Result<Map<String, String>> uploadDocument(@RequestParam("file") MultipartFile file) {
        Map<String, String> result = fileUploadService.uploadFile(file, "document");
        return Result.success(result);
    }

    @DeleteMapping
    public Result<?> deleteFile(@RequestParam String path) {
        fileUploadService.deleteFile(path);
        return Result.success();
    }
}