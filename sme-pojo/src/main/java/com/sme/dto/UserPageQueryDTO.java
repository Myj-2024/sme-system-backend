package com.sme.dto;

import com.sme.constant.StatusConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageNum;

    private int pageSize;



    /**
     * 用户账号（登录用）
     */
    private String username;

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
     * 状态：0-禁用 / 1-启用
     */
    private Integer status;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 所属部门编码
     */
    private String deptCode;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色ID（关联角色表，可扩展sys_role）
     */
    private Long roleId;

    private String roleCode;

}
