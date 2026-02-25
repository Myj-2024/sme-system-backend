package com.sme.controller;

import com.sme.result.Result;
import com.sme.utils.MinioUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制器
 * 处理图片/视频等文件上传到MinIO的请求
 */
@Slf4j
@RestController
@RequestMapping("/upload")
@Tag(name = "文件上传接口", description = "文件上传到MinIO的相关接口")
public class UploadController {

    @Autowired
    private MinioUtil minioUtil;  // 注入MinIO工具类

    /**
     * 文件上传到MinIO
     *
     * @param file 上传的文件（表单参数名必须为file）
     * @return 包含文件访问URL的统一响应结果
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "文件上传", description = "将本地文件上传到MinIO，并返回文件访问URL")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        log.info("开始处理文件上传，文件名：{}，文件大小：{}字节",
                file.getOriginalFilename(), file.getSize());

        // 1. 校验文件是否为空
        if (file.isEmpty()) {
            log.warn("文件上传失败：上传的文件为空");
            return Result.error("上传文件不能为空");
        }

        try {
            // 2. 调用MinIO工具类上传文件，获取访问URL
            String filePath = minioUtil.upload(file.getBytes(), file.getOriginalFilename());
            log.info("MinIO文件上传成功，访问路径：{}", filePath);

            // 3. 返回成功结果：URL放在data字段，message为默认"操作成功"
            return Result.success("操作成功",filePath);

        } catch (Exception e) {
            // 4. 异常处理：记录详细日志，返回友好提示
            log.error("MinIO文件上传失败，文件名：{}，异常信息：{}",
                    file.getOriginalFilename(), e.getMessage(), e);
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }
}