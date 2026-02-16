package com.sme.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PolicyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 政策标题
     */
    private String title;

    /**
     * 政策类型（关联sys_dict_item.item_code，字典编码policy_type）
     * 例如：FINANCE(金融扶持)、TAX(税收优惠)、INDUSTRY(产业扶持)
     */
    private String policyType;

    /**
     * 发布人ID，关联sys_user.id
     */
    private Long publisherId;

    /**
     * 政策内容（富文本存HTML）
     */
    private String content;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 是否置顶：0-否 / 1-是
     */
    private Integer isTop;

    /**
     * 是否显示：0-隐藏 / 1-显示
     */
    private Integer isShow;


    /**
     * 政策类型名称（从字典项sys_dict_item.item_name获取）
     */
    private String policyTypeName;

    /**
     * 发布人姓名（从sys_user.real_name获取）
     */
    private String publisherName;

}

