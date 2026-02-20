package com.sme.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RoleDTO {

    private Long id;               // 主键ID
    private String roleName;    // 将 name 改为 roleName
    private String roleCode;         // 角色编码
    private String description;    // 描述
    private Byte delFlag;          // 逻辑删除
    private Date createTime;       // 创建时间
    private Date updateTime;       // 更新时间

    /**
     * 权限ID列表，用于角色分配权限
     */
    private List<Long> permissionIds;
}
