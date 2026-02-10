package com.sme.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class EnterpriseVO {
    private Long id;  // 主键ID
    private String enterpriseName;  // 企业名称
    private String creditCode;  // 统一社会信用代码
    private String enterpriseTypeName;  // 企业类型名称 (可能关联字典表的名称)
    private String businessAddr;  // 经营地址
    private String legalPerson;  // 法定代表人
    private String phone;  // 企业联系电话
    private BigDecimal regCapital;  // 注册资本
    private Date establishTime;  // 成立时间
    private String townName;  // 所属乡镇名称 (可能关联字典表的名称)
    private String industryName;  // 所属行业名称 (可能关联字典表的名称)
    private String businessStatusName;  // 经营状态名称 (可能关联字典表的名称)
    private Date createTime;  // 创建时间
    private Date updateTime;  // 更新时间
    private Integer delFlag;  // 逻辑删除标记

    // Getters and Setters
}

