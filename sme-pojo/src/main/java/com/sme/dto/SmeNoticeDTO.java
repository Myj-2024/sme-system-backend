package com.sme.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SmeNoticeDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知类型（NOTICE / WARNING）
     */
    private String noticeType;

    /**
     * 发布人ID（sys_user.id）
     */
    private Long publisherId;

    /**
     * 通知内容（富文本HTML）
     */
    private String content;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;


    /**
     * 逻辑删除：0-未删 / 1-已删
     */
    private Integer delFlag;

    // ===================== 新增核心字段 =====================
    /**
     * 目标用户类型：ALL(全员) / SPECIFIC_USER(指定用户)
     */
    private String targetType;

    /**
     * 指定用户ID列表（targetType为SPECIFIC_USER时必填）
     */
    private List<Long> targetUserIds;

    /**
     * 发布人姓名
     */
    private String publisherName;
}