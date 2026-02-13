package com.sme.entity;



import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 企业包抓联服务表
 */
@Data
public class SmePLE implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（自增）
     */
    private Long id;

    /**
     * 包抓领导姓名
     */
    private String leaderName;

    /**
     * 包联企业ID（关联sme_enterprise.id）
     */
    private Long enterpriseId;

    /**
     * 企业名称（冗余字段）
     */
    private String enterpriseName;

    /**
     * 企业负责人（冗余字段，对应原表legal_person）
     */
    private String enterpriseLeader;

    /**
     * 企业联系电话（冗余字段）
     */
    private String enterprisePhone;

    /**
     * 注册地址（冗余字段，对应原表business_addr）
     */
    private String regAddr;

    /**
     * 主要产品（冗余字段）
     */
    private String mainProduct;

    /**
     * 专班班长（冗余字段）
     */
    private String classMonitor;

    /**
     * 专班联系电话（冗余字段）
     */
    private String classPhone;

    /**
     * 专班负责单位ID（关联sys_dept.id）
     */
    private Long classDeptId;

    /**
     * 专班负责单位名称（冗余字段）
     */
    private String classDeptName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除：0-未删 / 1-已删
     */
    private Integer delFlag;
}

