package com.sme.entity;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SmeNoticeUser implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 通知ID
     */
    private Long noticeId;

    /**
     * 接收人ID
     */
    private Long userId;

    /**
     * 是否已读：0-未读 / 1-已读
     */
    private Integer isRead;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}

