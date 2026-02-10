package com.sme.dto;

import lombok.Data;

import java.util.List;

@Data
public class PermissionDTO {

    /** 权限ID（新增为空，修改必传） */
    private Long id;

    /** 权限名称 */
    private String name;

    /** 权限编码 */
    private String code;

    /** 1-菜单  2-按钮 */
    private Integer type;

    /** 父权限ID */
    private Long parentId;

    /** 路由地址（菜单） */
    private String path;

    /** 前端组件路径 */
    private String component;

    /** 菜单图标 */
    private String icon;

    /** 排序 */
    private Integer sort;

    /** 状态：0-禁用 1-启用 */
    private Integer status;

    /** 角色授权用：权限ID集合 */
    private List<Long> permissionIds;
}
