package com.sme.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.security.Permission;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data

public class Role extends BaseEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色编码
     */

    private String code;
    /**
     * 描述
     */
    private String description;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 权限列表
     */
    private List<Permission> permissions;
}
