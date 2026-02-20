package com.sme.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.security.Permission;
import java.util.Date;
import java.util.List;


@Data

public class Role{

    private static final long serialVersionUID = 1L;

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
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 描述
     */
    private String description;

    /**
     * 权限列表
     */
    private List<Permission> permissions;
}
