package com.sme.service.impl;

import com.sme.mapper.SmeNoticeUserMapper;
import com.sme.service.SmeNoticeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 通知-用户关联服务实现类
 */
@Service
public class SmeNoticeUserServiceImpl implements SmeNoticeUserService {

    @Autowired
    private SmeNoticeUserMapper noticeUserMapper;

    /**
     * 标记通知已读
     */
    @Override
    public void markAsRead(Long noticeId, Long userId) {
        // 调用Mapper的标记已读方法
        noticeUserMapper.markAsRead(noticeId, userId);
    }

    /**
     * 查询用户未读通知数量
     */
    @Override
    public Integer unreadCount(Long userId) {
        // 调用Mapper的统计未读数量方法
        return noticeUserMapper.countUnread(userId);
    }
}