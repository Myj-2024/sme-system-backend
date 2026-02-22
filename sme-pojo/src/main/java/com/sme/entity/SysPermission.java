package com.sme.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
public class SysPermission {

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


    // 新增路由配置字段
    private String componentPath;       // 前端组件路径
    private String redirectPath;        // 路由重定向路径
    private String activeMenu;          // 高亮菜单路径
    private String routeName;           // 路由名称（唯一标识）
    private Integer isHidden;           // 是否隐藏路由：0-显示/1-隐藏

}
