package com.survey.controller;

import com.survey.dto.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@Tag(name = "文件管理", description = "文件上传下载")
public class FileController {

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    @Value("${file.base-url:http://localhost:8080/api/files}")
    private String baseUrl;

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error(400, "请选择要上传的文件");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.error(400, "只支持上传图片文件");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            return Result.error(400, "文件大小不能超过5MB");
        }

        try {
            String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String uploadPath = uploadDir + "/" + datePath;
            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString().replace("-", "") + extension;

            Path filePath = Paths.get(uploadPath, fileName);
            Files.write(filePath, file.getBytes());

            String fileUrl = baseUrl + "/" + datePath + "/" + fileName;
            return Result.success(fileUrl);

        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(500, "文件上传失败: " + e.getMessage());
        }
    }

    @PostMapping("/upload/multiple")
    @Operation(summary = "批量上传文件")
    public Result<String[]> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return Result.error(400, "请选择要上传的文件");
        }

        String[] urls = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            Result<String> result = uploadFile(files[i]);
            if (result.getCode() == 200) {
                urls[i] = result.getData();
            } else {
                return Result.error(400, "第 " + (i + 1) + " 个文件上传失败");
            }
        }
        return Result.success(urls);
    }

    @DeleteMapping("/{path}/{filename}")
    @Operation(summary = "删除文件")
    public Result<Void> deleteFile(@PathVariable String path, @PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir, path, filename);
            Files.deleteIfExists(filePath);
            return Result.success();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(500, "文件删除失败");
        }
    }
}
