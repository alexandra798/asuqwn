// FileUploadService.java
package com.forum.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface FileUploadService {
    Map<String, String> uploadFile(MultipartFile file, String type);
    void deleteFile(String filePath);
    boolean validateFile(MultipartFile file, String type);
}