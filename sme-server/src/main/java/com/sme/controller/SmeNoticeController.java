package com.sme.controller;

import com.sme.dto.SmeNoticeDTO;
import com.sme.dto.SmeNoticePageQueryDTO;
import com.sme.entity.SmeNotice;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.SmeNoticeService;
import com.sme.utils.UserContext; // 新增：引入项目统一的用户上下文工具类
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/notice")
@Slf4j
@Tag(name = "通知管理")
public class SmeNoticeController {

    @Autowired
    private SmeNoticeService smeNoticeService;

    /**
     * 1. 分页查询通知
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询通知")
    public Result<PageResult> page(SmeNoticePageQueryDTO dto) {
        PageResult page = smeNoticeService.pageQuery(dto);
        return Result.success(page);
    }

    /**
     * 2. 根据ID查询详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询详情")
    public Result<SmeNotice> detail(@PathVariable Long id) {
        SmeNotice notice = smeNoticeService.getById(id);
        return Result.success(notice);
    }

    /**
     * 3. 新增通知
     */
    @PostMapping("/save")
    @Operation(summary = "新增通知")
    public Result<?> save(@RequestBody SmeNoticeDTO dto) {
        // 修复：改用UserContext获取当前登录用户ID（和用户模块保持一致）
        Long publisherId = UserContext.getUserId();
        if (publisherId == null) {
            return Result.error("未登录");
        }

        dto.setPublisherId(publisherId);
        smeNoticeService.saveNotice(dto);
        return Result.success("通知发布成功");
    }

    /**
     * 4. 修改通知
     */
    @PostMapping("/update")
    @Operation(summary = "修改通知")
    public Result<?> update(@RequestBody SmeNoticeDTO dto) {
        smeNoticeService.updateNotice(dto);
        return Result.success();
    }

    /**
     * 5. 删除通知
     */
    @PostMapping("/delete/{id}")
    @Operation(summary = "删除通知")
    public Result<?> delete(@PathVariable Long id) {
        smeNoticeService.deleteById(id);
        return Result.success();
    }

    /**
     * 6. 获取当前用户通知列表
     */
    @GetMapping("/my")
    @Operation(summary = "获取当前用户通知列表")
    public Result<PageResult> myNotice(@RequestParam Integer pageNum,
                                       @RequestParam Integer pageSize) {
        // 核心修复：改用项目统一的UserContext获取用户ID（避免操作SecurityContextHolder的错误）
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.error("未登录");
        }

        PageResult page = smeNoticeService.queryMyNotice(userId, pageNum, pageSize);
        return Result.success(page);
    }
}