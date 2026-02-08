package com.sme.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DictItem {

    private Long id;                // 主键ID
    private Long dictId;            // 关联字典主表ID
    private String itemCode;        // 项编码
    private String itemName;        // 项名称
    private Integer sort;           // 排序
    private Integer status;         // 状态：0-禁用/1-启用
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private Integer delFlag;
}
