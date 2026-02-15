package com.sme.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sme.dto.PolicyDTO;
import com.sme.dto.PolicyPageQueryDTO;
import com.sme.entity.Policy;
import com.sme.mapper.PolicyMapper;
import com.sme.result.PageResult;
import com.sme.service.PolicyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PolicyServiceImpl implements PolicyService {

    @Autowired
    private PolicyMapper policyMapper;


    /**
     * 分页查询
     * @param policyPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(PolicyPageQueryDTO policyPageQueryDTO) {
        PageHelper.startPage(policyPageQueryDTO.getPageNum(), policyPageQueryDTO.getPageSize());
        Page<Policy> page = policyMapper.pageQuery(policyPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增政策
     * @param policyDTO
     */
    @Override
    public void createPolicy(PolicyDTO policyDTO) {
        Policy policy = new Policy();
        BeanUtils.copyProperties(policyDTO, policy);
        policyMapper.insert( policy);
    }
}
