package com.sme.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户角色关联
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRole extends BaseEntity{

    private static final long serialVersionUID = 1L;


    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;
}
