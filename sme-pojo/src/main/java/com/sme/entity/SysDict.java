package com.sme.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor  // 必须有：无参构造
@AllArgsConstructor
public class SysDict implements Serializable {
    private Long id;
    private String dictCode;
    private String dictName;
    private Integer sort;
    private Integer status; // 0-禁用, 1-启用
    private Date createTime;
    private Date updateTime;
    private Integer delFlag; // 0-未删, 1-已删
}
