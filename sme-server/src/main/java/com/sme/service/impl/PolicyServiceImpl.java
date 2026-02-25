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

import java.util.List;

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


    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public Policy getPolicyById(Long id) {
        return policyMapper.getPolicyById(id);
    }

    /**
     * 修改
     * @param policyDTO
     * @return
     */
    @Override
    public boolean updatePolicy(PolicyDTO policyDTO) {
        return policyMapper.updatePolicy(policyDTO) > 0;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    public boolean deletePolicy(Long id) {
        return policyMapper.deletePolicy(id) > 0;
    }

    /**
     * 批量显示
     * @param ids
     * @return
     */
    @Override
    public boolean batchShowPolicies(List<Long> ids) {
        return policyMapper.batchShowPolicies(ids) > 0;
    }

    /**
     * 新增：查询所有已隐藏政策ID（del_flag=1 且 is_show=0）
     * 解决前端分页过滤后拿不到已隐藏ID的问题
     */
    @Override
    public List<Long> getHiddenPolicyIds() {
        return policyMapper.selectHiddenPolicyIds();
    }
}

