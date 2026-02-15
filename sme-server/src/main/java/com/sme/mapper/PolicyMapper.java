package com.sme.mapper;

import com.github.pagehelper.Page;
import com.sme.dto.PolicyDTO;
import com.sme.dto.PolicyPageQueryDTO;
import com.sme.entity.Policy;
import org.apache.ibatis.annotations.Mapper;

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
}
