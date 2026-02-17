package com.sme.entity;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SmeNotice implements Serializable {

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
     * 是否置顶：0-否 / 1-是
     */
    private Integer isTop;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

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
     * 目标用户值：指定用户时为逗号分隔的用户ID，全员时为空
     */
    private String targetValue;

    // ===================== 新增字段 =====================
    /**
     * 是否已读（仅查询当前用户通知时生效）：0-未读 / 1-已读
     */
    private Integer isRead;

    /**
     * 发布人姓名
     */
    private String publisherName;
}

