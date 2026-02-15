package com.sme.service;

import com.sme.dto.PolicyDTO;
import com.sme.dto.PolicyPageQueryDTO;
import com.sme.result.PageResult;

public interface PolicyService {

    /**
     * 分页查询
     * @param policyPageQueryDTO
     * @return
     */
    PageResult pageQuery(PolicyPageQueryDTO policyPageQueryDTO);

    /**
     * 新增政策
     * @param policyDTO
     */
    void createPolicy(PolicyDTO policyDTO);
}
