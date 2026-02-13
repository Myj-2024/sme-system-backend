package com.sme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmePackageContactPageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageNum;

    private int pageSize;

    /** 流程状态：UNHANDLED-未受理 HANDLING-办理中 COMPLETED-已办结 UNABLE-无法办理 */
    private String processStatus;

    /** 包抓领导姓名 */
    private String leaderName;

    /** 企业名称（冗余，来自sme_lpe.enterprise_name） */
    private String enterpriseName;

    /** 专班班长 */
    private String classMonitor;

    /** 专班负责单位名称（冗余，来自sme_lpe.class_dept_name） */
    private String classDeptName;

    /** 问题类型，关联sys_dict_item.item_code */
    private String problemType;


}
