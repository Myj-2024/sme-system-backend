package com.sme.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
public class SysPermission{

    /**
     * 主键ID（自增）
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除：0-未删 / 1-已删
     */
    private Byte delFlag = 0;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限编码
     */
    private String code;

    /**
     * 权限类型
     * 1:菜单 2:按钮
     */
    private Integer type;

    /**
     * 父级权限ID
     */
    private Long parentId;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 权限图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态
     * 0:禁用 1:正常
     */
    private Byte status;

    /**
     * 子权限列表 (非数据库字段)
     */
    private List<SysPermission> children = new ArrayList<>();
}
