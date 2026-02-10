package com.sme.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
public class SysDictVO implements Serializable {
    private Long id;
    private String dictName;
    private String dictCode;
    private Integer status;
    private Date createTime;
    // 若依特色：搜索时间范围
    private Map<String, Object> params; // 存放 beginTime, endTime
}