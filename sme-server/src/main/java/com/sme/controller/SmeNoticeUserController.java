package com.sme.controller;

import com.sme.result.Result;
import com.sme.service.SmeNoticeUserService;
import com.sme.utils.UserContext; // 引入项目统一的用户上下文工具类
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 通知-用户关联控制器（优化版）
 * 移除重复的登录校验逻辑，统一使用UserContext获取用户ID
 */
@RestController
@RequestMapping("/admin/noticeUser")
public class SmeNoticeUserController {

    @Autowired
    private SmeNoticeUserService noticeUserService;

    /**
     * 抽取通用的获取当前登录用户ID方法（消除代码重复）
     */
    private Long getCurrentUserId() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new RuntimeException("未登录"); // 抛出运行时异常，统一全局异常处理
        }
        return userId;
    }

    /**
     * 标记通知已读
     */
    @PostMapping("/read")
    public Result<?> markAsRead(@RequestParam Long noticeId) {
        try {
            Long userId = getCurrentUserId(); // 复用通用方法
            noticeUserService.markAsRead(noticeId, userId);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取当前用户未读数量
     */
    @GetMapping("/unreadCount")
    public Result<Integer> unreadCount() {
        try {
            Long userId = getCurrentUserId(); // 复用通用方法
            Integer count = noticeUserService.unreadCount(userId);
            return Result.success(count);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}