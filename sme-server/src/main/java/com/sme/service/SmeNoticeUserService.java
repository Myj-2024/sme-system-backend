package com.sme.service;

import org.springframework.stereotype.Service;

/**
 * 通知-用户关联服务接口
 */

public interface SmeNoticeUserService {

    /**
     * 标记通知已读
     * @param noticeId 通知ID
     * @param userId 用户ID
     */
    void markAsRead(Long noticeId, Long userId);

    /**
     * 查询用户未读通知数量
     * @param userId 用户ID
     * @return 未读数量
     */
    Integer unreadCount(Long userId);
}