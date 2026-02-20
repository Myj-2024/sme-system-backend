package com.sme.dto;

import lombok.Data;

@Data
public class PermissionPageQueryDTO {

    private Integer pageNum;
    private Integer pageSize;
    private String name;
    private String code;

    /**
     * 图标路径
     */
    private String iconUrl;

    private Long iconId;
}
