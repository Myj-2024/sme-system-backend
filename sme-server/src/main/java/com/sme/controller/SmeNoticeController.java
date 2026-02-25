package com.sme.controller;

import com.sme.dto.SmeNoticeDTO;
import com.sme.dto.SmeNoticePageQueryDTO;
import com.sme.entity.SmeNotice;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.service.SmeNoticeService;
import com.sme.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/notice")
@Slf4j
@Tag(name = "通知管理", description = "通知管理接口")
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
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.error("未登录");
        }

        PageResult page = smeNoticeService.queryMyNotice(userId, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 7. 获取当前用户发送给别人的通知列表（我发送的通知）
     */
    @GetMapping("/sent")
    @Operation(summary = "获取当前用户发送给别人的通知列表")
    public Result<PageResult> sentNotice(
            @Parameter(description = "页码", required = true) @RequestParam Integer pageNum,
            @Parameter(description = "每页条数", required = true) @RequestParam Integer pageSize,
            @Parameter(description = "通知标题（模糊查询）") @RequestParam(required = false) String title
    ) {
        // 1. 获取当前登录用户ID
        Long publisherId = UserContext.getUserId();
        if (publisherId == null) {
            log.warn("查询我发送的通知失败：用户未登录");
            return Result.error("未登录");
        }

        // 2. 调用service查询我发送的通知
        PageResult page = smeNoticeService.querySentNotice(publisherId, pageNum, pageSize, title);
        log.info("查询当前用户{}发送的通知，页码{}，每页{}条，标题筛选{}，结果总数{}",
                publisherId, pageNum, pageSize, title, page.getTotal());

        return Result.success(page);
    }
}