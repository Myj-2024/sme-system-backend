package com.sme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmePackageContactDTO {

    /** 主键ID（自增） */
    private Long id;

    /** 包抓联编号（系统自动生成） */
    private String packageNo;

    /** 流程状态：UNHANDLED-未受理 HANDLING-办理中 COMPLETED-已办结 UNABLE-无法办理 */
    private String processStatus;

    /** 包联部门ID，关联sys_dept.id（保留原有字段） */
    private Long deptId;

    /** 包抓领导姓名 */
    private String leaderName;

    /** 包联企业ID，关联sme_enterprise.id */
    private Long enterpriseId;

    /** 企业名称（冗余，来自sme_lpe.enterprise_name） */
    private String enterpriseName;

    /** 企业负责人（冗余，来自sme_lpe.enterprise_leader） */
    private String enterpriseLeader;

    /** 企业联系电话（冗余，来自sme_lpe.enterprise_phone） */
    private String enterprisePhone;

    /** 专班班长 */
    private String classMonitor;


    /** 专班负责单位名称（冗余，来自sme_lpe.class_dept_name） */
    private String classDeptName;

    /** 专班联系电话（冗余，来自sme_lpe.class_phone） */
    private String classPhone;

    /** 包联企业反映的问题 */
    private String enterpriseProblem;

    /** 问题接收时间 */
    private LocalDateTime problemReceiveTime;

    /** 问题类型，关联sys_dict_item.item_code */
    private String problemType;

    /** 办理结果，关联sys_dict_item.item_code */
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


}
