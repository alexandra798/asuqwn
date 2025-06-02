// FileUploadServiceImpl.java
package com.forum.service.impl;

import com.forum.exception.BusinessException;
import com.forum.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.max-size:10485760}") // 10MB
    private long maxFileSize;

    private static final Map<String, String[]> ALLOWED_TYPES = new HashMap<>();

    static {
        ALLOWED_TYPES.put("image", new String[]{".jpg", ".jpeg", ".png", ".gif", ".webp"});
        ALLOWED_TYPES.put("document", new String[]{".pdf", ".doc", ".docx", ".xls", ".xlsx"});
        ALLOWED_TYPES.put("payment", new String[]{".jpg", ".jpeg", ".png", ".pdf"});
    }

    @Override
    public Map<String, String> uploadFile(MultipartFile file, String type) {
        if (!validateFile(file, type)) {
            throw new BusinessException("文件验证失败");
        }

        try {
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = generateFilename(extension);

            // 创建目录
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path directoryPath = Paths.get(uploadPath, type, datePath);
            Files.createDirectories(directoryPath);

            // 保存文件
            Path filePath = directoryPath.resolve(newFilename);
            file.transferTo(filePath.toFile());

            // 返回文件信息
            Map<String, String> result = new HashMap<>();
            result.put("filename", newFilename);
            result.put("originalFilename", originalFilename);
            result.put("path", "/" + type + "/" + datePath + "/" + newFilename);
            result.put("size", String.valueOf(file.getSize()));
            result.put("type", file.getContentType());

            log.info("文件上传成功: {}", result);
            return result;

        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败");
        }
    }

    @Override
    public void deleteFile(String filePath) {
        try {
            Path path = Paths.get(uploadPath, filePath);
            Files.deleteIfExists(path);
            log.info("文件删除成功: {}", filePath);
        } catch (IOException e) {
            log.error("文件删除失败", e);
        }
    }

    @Override
    public boolean validateFile(MultipartFile file, String type) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // 检查文件大小
        if (file.getSize() > maxFileSize) {
            throw new BusinessException("文件大小超过限制");
        }

        // 检查文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return false;
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        String[] allowedExtensions = ALLOWED_TYPES.get(type);

        if (allowedExtensions == null) {
            return false;
        }

        for (String allowed : allowedExtensions) {
            if (allowed.equals(extension)) {
                return true;
            }
        }

        return false;
    }

    private String generateFilename(String extension) {
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }
}