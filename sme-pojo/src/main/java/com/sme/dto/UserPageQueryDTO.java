package com.sme.dto;

import com.sme.constant.StatusConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
     * 状态：0-禁用 / 1-启用
     */
    private Integer status;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 所属部门ID，关联sys_dept表
     */
    private Long deptId;
}
