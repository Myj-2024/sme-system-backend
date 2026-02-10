package com.sme.dto;

import lombok.Data;

@Data
public class DeptPageQueryDTO {

    private int pageNum;

    private int pageSize;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门负责人
     */
    private String leader;

    /**
     * 职务
     */
    private String position;

    /**
     * 部门状态
     */
    private Integer status;

    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 联系电话
     */
    private String phone;
}
