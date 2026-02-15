package com.sme.controller;

import com.sme.constant.MessageConstant;
import com.sme.result.Result;
import com.sme.utils.MinioUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@Tag(name = "文件上传", description = "文件上传相关接口")
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private MinioUtil minioUtil;  // 注入MinIO工具类

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Tag(name = "文件上传", description = "文件上传相关接口")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        log.info("文件上传:{}",file);
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }
        try {
            log.info("MinIO文件上传：{}", file.getOriginalFilename());
            String filePath = minioUtil.upload(file.getBytes(), file.getOriginalFilename());
            log.info("MinIO文件上传完成，访问路径：{}", filePath);
            return Result.success(filePath);
        } catch (Exception e) {
            log.error("MinIO文件上传失败：{}", e.getMessage());
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

}
