package com.sme.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RoleVO {

    private Long id;               // 主键ID
    private String name;           // 角色名称
    private String code;           // 角色编码
    private String description;    // 描述
    private Integer status;        // 状态
    private Byte delFlag;          // 逻辑删除
    private Date createTime;       // 创建时间
    private Date updateTime;       // 更新时间

    /**
     * 权限列表，包含前端展示的完整信息
     */
    private List<PermissionVO> permissions;
}
