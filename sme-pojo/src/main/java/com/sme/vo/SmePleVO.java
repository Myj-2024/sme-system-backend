package com.sme.vo;



import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SmePleVO {

    private Long id;

    private String packageNo;

    private Long deptId;
    private String deptName;              // 联表查询

    private String leaderItemCode;
    private String leaderName;

    private Long enterpriseId;
    private String enterpriseName;        // 联表查询

    private String classMonitor;

    private Long classDeptId;
    private String classDeptName;         // 联表查询

    private String enterpriseProblem;

    private LocalDateTime problemReceiveTime;

    private String problemType;
    private String problemTypeName;       // 字典名称

    private String handleResult;
    private String handleResultName;      // 字典名称

    private LocalDateTime completeTime;

    private String handleRemark;

    private String remark;

    private LocalDateTime createTime;
}


