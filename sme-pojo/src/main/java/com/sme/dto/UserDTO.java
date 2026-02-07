package com.sme.dto;

import com.sme.constant.StatusConstant;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户DTO
 */
@Data
public class UserDTO implements Serializable {


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
     * 所属部门ID，关联sys_dept表
     */
    private Long deptId;

    /**
     * 角色ID（关联角色表，可扩展sys_role）
     */
    private Long roleId = 0L;

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
}
