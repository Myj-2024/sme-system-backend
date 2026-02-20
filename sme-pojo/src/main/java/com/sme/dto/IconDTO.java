package com.sme.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class IconDTO implements Serializable {

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
}
