package com.sme.vo;

import lombok.Data;

import java.util.List;

@Data
public class PermissionVO {

    /** 权限ID */
    private Long id;

    /** 权限名称 */
    private String name;

    /** 权限编码（按钮 / 接口鉴权） */
    private String code;

    /** 1-菜单  2-按钮 */
    private Integer type;

    /** 父权限ID */
    private Long parentId;

    /** 路由地址 */
    private String path;

    /** 前端组件路径 */
    private String component;

    /** 菜单图标 */
    private String icon;

    /** 排序 */
    private Integer sort;

    /** 子权限（菜单树） */
    private List<PermissionVO> children;
}

