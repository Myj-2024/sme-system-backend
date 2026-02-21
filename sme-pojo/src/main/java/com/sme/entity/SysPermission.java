package com.sme.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
public class SysPermission{

    private Long id;
    private String name;
    private String code;
    private Integer type;
    private String path;
    private Integer sort;
    private Date createTime;
    private Date updateTime;
    private Integer delFlag;
    /**
     * 图标路径
     */
    private String iconUrl;

    private Long iconId;


   private String iconCode;
   private Long parentId;
    private Integer isRoute;
}
