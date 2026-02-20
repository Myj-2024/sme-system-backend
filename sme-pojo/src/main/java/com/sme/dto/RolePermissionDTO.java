package com.sme.dto;

import lombok.Data;
import java.util.List;

/**
 * 角色权限分配DTO
 */
@Data
public class RolePermissionDTO {
    private Long roleId;                  // 角色ID
    private List<Long> permissionIds;     // 要分配的菜单ID列表
}