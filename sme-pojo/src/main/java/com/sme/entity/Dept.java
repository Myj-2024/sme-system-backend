package com.sme.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Dept implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    private Long id;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 上级部门ID
     */
    private Long parentId;

    /**
     * 部门负责人
     */
    private String leader;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 职务
     */
    private String position;

    /**
     * 部门状态
     */
    private Integer status;

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
