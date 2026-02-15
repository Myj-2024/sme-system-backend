package com.sme.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SmePackageContact implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID（自增） */
    private Long id;

    /** 问题编号（系统自动生成） */
    private String packageNo;

    /** 流程状态：UNHANDLED-未受理 HANDLING-办理中 COMPLETED-已办结 UNABLE-无法办理 */
    private String processStatus;

    /** 关联 lpe 表 */
    private Long lpeId;

    /** 包抓领导姓名 */
    private String leaderName;

    /** 包联企业ID，关联sme_enterprise.id */
    private Long enterpriseId;

    /** 企业名称 */
    private String enterpriseName;

    /** 企业负责人 */
    private String enterpriseLeader;

    /** 企业联系电话 */
    private String enterprisePhone;

    /** 专班班长 */
    private String classMonitor;


    /** 专班负责单位名称 */
    private String classDeptName;

    /** 专班联系电话 */
    private String classPhone;

    /** 包联企业反映的问题 */
    private String enterpriseProblem;

    /** 问题接收时间 */
    private LocalDateTime problemReceiveTime;

    /** 问题类型 */
    private String problemType;

    /** 办理结果 */
    private String handleResult;

    /** 办理过程/办理情况 */
    private String handleContent;

    /** 无法办理说明（process_status=UNABLE时必填） */
    private String unableReason;

    /** 办结时间（未办结为NULL） */
    private LocalDateTime completeTime;

    /** 办理详细说明 */
    private String handleRemark;

    /** 备注（办理人=包抓领导leader_name） */
    private String remark;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除：0-未删 / 1-已删 */
    private Integer delFlag;
}