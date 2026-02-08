package com.sme.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Dict {
    private Long id;                // 主键ID
    private String dictCode;        // 字典编码（唯一）
    private String dictName;        // 字典名称
    private Integer sort;           // 排序
    private Integer status;         // 状态：0-禁用/1-启用
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private Integer delFlag;
}
