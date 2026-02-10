package com.sme.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysDictItemVO implements Serializable {
    private Long id;
    private Long dictId;
    private String dictCode; // 关联查询出编码
    private String itemName;
    private String itemCode;
    private Integer sort;
    private Integer status;
    private Date createTime;
}
