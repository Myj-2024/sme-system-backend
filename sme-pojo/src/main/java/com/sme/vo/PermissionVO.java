package com.sme.vo;

import lombok.Data;
import java.util.List;

/**
 * 菜单权限VO（适配前端动态路由/菜单渲染）
 */
@Data
public class PermissionVO {
    // 基础字段（对应sys_permission表）
    private Long id;
    private String name;        // 菜单名称（前端meta.title）
    private String path;        // 路由地址（前端router.path）
    private Integer type;       // 权限类型：1-菜单/2-按钮
    private Long parentId;      // 父菜单ID
    private String iconCode;    // 图标编码（如icon-home）
    private String iconUrl;     // 自定义图标URL
    private Integer sort;       // 排序

    // 前端路由专用字段
    private String component;   // 前端组件路径（如@/views/enterprise/index.vue）
    private List<PermissionVO> children; // 子菜单
    private Integer isRoute;

    // 前端路由meta配置
    private MetaVO meta;

    /**
     * 路由元信息
     */
    @Data
    public static class MetaVO {
        private String title;    // 菜单标题
        private String icon;     // 图标（兼容Element Plus）
        private String activeMenu; // 面包屑高亮路径
    }
}