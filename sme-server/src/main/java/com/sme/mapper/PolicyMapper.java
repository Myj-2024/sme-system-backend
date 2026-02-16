package com.sme.mapper;

import com.github.pagehelper.Page;
import com.sme.dto.PolicyDTO;
import com.sme.dto.PolicyPageQueryDTO;
import com.sme.entity.Policy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PolicyMapper {


    /**
     * 分页查询
     * @param policyPageQueryDTO
     * @return
     */
    Page<Policy> pageQuery(PolicyPageQueryDTO policyPageQueryDTO);

    /**
     * 新增
     * @param policy
     */
    void insert(Policy policy);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    Policy getPolicyById(Long id);

    /**
     * 修改
     * @param policyDTO
     */
    int updatePolicy(PolicyDTO policyDTO);

    /**
     * 删除
     * @param id
     */
    int deletePolicy(Long id);

    /**
     * 批量显示
     * @param ids
     */
    int batchShowPolicies(List<Long> ids);

    /**
     * 查询所有已隐藏政策ID（del_flag=1 且 is_show=0）
     * @return
     */
    List<Long> selectHiddenPolicyIds();
}
