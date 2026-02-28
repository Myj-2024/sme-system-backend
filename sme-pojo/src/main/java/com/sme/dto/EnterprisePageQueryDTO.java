package com.sme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterprisePageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageNum;

    private int pageSize;

    private String enterpriseName;

    private String enterpriseType;

    private String townId;

    private String industryId;

    private String businessStatus;

    private String mainProduct;

    private String enterpriseIntro;

    private Integer isShow;

}
