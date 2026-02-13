package com.sme.entity;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SmePackageHandleRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 办理记录主键ID */
    private Long id;
    /** 关联包抓联主表ID（sme_package_contact.id） */
    private Long packageId;
    /** 本次办理领导（关联sme_package_contact.leader_name） */
    private String handleLeader;
    /** 本次办理时间 */
    private LocalDateTime handleTime;
    /** 本次办理内容/跟进情况 */
    private String handleContent;
    /** 办理类型：FOLLOW-跟进、COMMUNICATE-沟通、SUBMIT-提交材料、REPLY-回复企业等 */
    private String handleType;
    /** 办理附件URL（多个附件用逗号分隔） */
    private String attachUrl;
    /** 记录创建时间 */
    private LocalDateTime createTime;
    /** 逻辑删除：0-未删/1-已删 */
    private Integer delFlag;
}