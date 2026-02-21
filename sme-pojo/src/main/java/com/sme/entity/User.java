package com.sme.entity;

import com.sme.constant.StatusConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户实体类",name = "User")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键ID（自增）
     */
    private Long id;

    /**
     * 用户账号（登录用）
     */
    private String username;

    /**
     * 密码（BCrypt加密存储）
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号（可做唯一校验）
     */
    private String phone = "";

    /**
     * 头像
     */
    private String avatar;


    /**
     * 所属部门编码
     */
    private String deptCode;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 角色ID（关联角色表，可扩展sys_role）
     */
    private Long roleId;

    /**
     * 状态：0-禁用 / 1-启用
     */
    private Integer status = StatusConstant.ENABLE;

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

    private List<Role> roles;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

}
