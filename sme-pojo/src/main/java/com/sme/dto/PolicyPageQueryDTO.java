package com.sme.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PolicyPageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private int pageNum;

    private int pageSize;


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
     * 发布人姓名（从sys_user.real_name获取）
     */
    private String publisherName;

    /**
     * 政策内容（富文本存HTML）
     */
    private String content;

    /**
     * 政策类型名称（从字典项sys_dict_item.item_name获取）
     */
    private String policyTypeName;

}
