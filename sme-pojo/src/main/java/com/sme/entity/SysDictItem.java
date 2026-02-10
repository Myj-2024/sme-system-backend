package com.sme.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysDictItem implements Serializable {
    private Long id;
    private Long dictId;
    private String itemCode;
    private String itemName;
    private Integer sort;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private Integer delFlag;
}
