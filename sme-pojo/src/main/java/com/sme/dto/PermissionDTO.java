package com.sme.dto;

import lombok.Data;

import java.util.List;

@Data
public class PermissionDTO {

    private Long id;
    private String name;
    private String code;
    private Integer type;
    private String path;
    private Integer sort;
    private String createTime;
    private String updateTime;
    private Integer delFlag;

    /**
     * 图标路径
     */
    private String iconUrl;
}
