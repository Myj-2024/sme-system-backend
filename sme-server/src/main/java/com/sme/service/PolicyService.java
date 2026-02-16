package com.sme.service;

import com.sme.dto.PolicyDTO;
import com.sme.dto.PolicyPageQueryDTO;
import com.sme.entity.Policy;
import com.sme.result.PageResult;

import java.util.List;

public interface PolicyService {

    /**
     * 分页查询
     * @param dto
     * @return
     */
    PageResult pageQuery(PolicyPageQueryDTO dto);

    /**
     * 新增政策
     * @param policyDTO
     */
    void createPolicy(PolicyDTO policyDTO);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Policy getPolicyById(Long id);

    /**
     * 修改政策
     * @param policyDTO
     */
    boolean updatePolicy(PolicyDTO policyDTO);

    /**
     * 删除政策
     * @param id
     */
    boolean deletePolicy(Long id);

    /**
     * 批量显示
     * @param ids
     * @return
     */
    boolean batchShowPolicies(List<Long> ids);

    /**
     * 查询所有已隐藏政策ID
     * @return
     */
    List<Long> getHiddenPolicyIds();
}
