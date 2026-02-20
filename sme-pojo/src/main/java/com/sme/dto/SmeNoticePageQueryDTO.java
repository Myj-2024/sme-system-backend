package com.sme.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SmeNoticePageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageNum;

    private int pageSize;

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
     * 发布人姓名
     */
    private String publisherName;
}
