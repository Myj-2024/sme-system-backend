package com.sme.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class IconPageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageNum;

    private int pageSize;

    /**
     * 图标名称
     */
    private String iconName;

    /**
     * 图标编码
     */
    private String iconCode;
}
