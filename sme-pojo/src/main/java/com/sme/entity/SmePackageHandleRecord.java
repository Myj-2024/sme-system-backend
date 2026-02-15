package com.sme.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 包抓联问题办理记录实体类（和数据库表100%匹配）
 */
@Data

public class SmePackageHandleRecord implements Serializable {
    /** 主键ID */

    private Long id;

    /** 关联问题ID（包抓联主表ID） */
    private Long packageId;

    /** 办理时间 */
    private LocalDateTime handleTime;


    /** 办理内容 */
    private String handleContent;

    /** 操作类型：ACCEPT(受理)/PROCESS(办理)/COMPLETE(办结)/UNABLE(无法办结) */
    private String handleType;

    /** 附件URL（多个用逗号分隔） */
    private String attachUrl;

    /** 创建时间（数据库默认CURRENT_TIMESTAMP） */
    private LocalDateTime createTime;

    /** 更新时间（数据库自动更新） */
    private LocalDateTime updateTime;

    /** 删除标记：0-未删除 1-已删除 */
    private Integer delFlag;
}