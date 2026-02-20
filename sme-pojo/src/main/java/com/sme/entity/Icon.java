package com.sme.entity;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
@Data
public class Icon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（自增）
     */
    private Long id;

    /**
     * 图标名称
     */
    private String iconName;

    /**
     * 图标编码
     */
    private String iconCode;

    /**
     * 图标路径
     */
    private String iconUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标记
     */
    private Integer delFlag;
}
