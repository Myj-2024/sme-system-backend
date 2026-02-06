package com.sme.entity;

import lombok.Data;

import java.util.Date;

/**
 * 基础实体类
 */
@Data
public class BaseEntity {

    /**
     * 主键ID（自增）
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除：0-未删 / 1-已删
     */
    private Byte delFlag = 0;
}
