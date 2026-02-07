package com.sme.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色权限关联
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RolePermission extends BaseEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 权限ID
     */
    private Long permissionId;
}
