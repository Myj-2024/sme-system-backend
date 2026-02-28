package com.sme.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class EnterpriseDTO {
    private Long id;  // 主键ID
    private String enterpriseName;  // 企业名称
    private String creditCode;  // 统一社会信用代码
    private String enterpriseType;  // 企业类型
    private String businessAddr;  // 经营地址
    private String legalPerson;  // 法定代表人
    private String phone;  // 企业联系电话
    private BigDecimal regCapital;  // 注册资本
    private Date establishTime;  // 成立时间
    private String townId;  // 所属乡镇
    private String industryId;  // 所属行业
    private String businessStatus;  // 经营状态
    private String enterpriseIntro;
    private Integer isShow;
    private Date createTime;  // 创建时间
    private Date updateTime;  // 更新时间
    private Integer delFlag;  // 逻辑删除标记

    // Getters and Setters
}

