package com.sme.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sme.dto.EnterprisePageQueryDTO;
import com.sme.entity.Enterprise;
import com.sme.mapper.EnterpriseMapper;
import com.sme.result.PageResult;
import com.sme.service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EnterpriseServiceImpl implements EnterpriseService {

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    /**
     * 分页查询
     * @return
     */


    @Override
    public PageResult page(EnterprisePageQueryDTO enterprisePageQueryDTO) {
        PageHelper.startPage(enterprisePageQueryDTO.getPageNum(), enterprisePageQueryDTO.getPageSize());
        Page<Enterprise> page = enterpriseMapper.selectPage(enterprisePageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增企业
     * @param enterprise
     */
    @Override
    public void insertEnterprise(Enterprise enterprise) {
        enterprise.setCreateTime(new Date());
        enterprise.setUpdateTime(new Date());
        enterpriseMapper.insertEnterprise(enterprise);
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Enterprise getById(Long id) {
        return enterpriseMapper.selectById(id);
    }

    @Override
    public void updateEnterprise(Enterprise enterprise) {
        enterprise.setUpdateTime(new Date());
        enterpriseMapper.updateEnterprise(enterprise);
    }

    /**
     * 删除企业
     * @param id
     */
    @Override
    public void deleteEnterprise(Long id) {
        enterpriseMapper.deleteById(id);
    }

    /**
     * 修改企业状态
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        enterpriseMapper.updateStatus(id, status);
    }

    /**
     * 是否显示
     * @param id
     * @param isShow
     * @return
     */
    @Override
    public boolean isShow(Long id, Integer isShow) {
        return enterpriseMapper.isShow(id, isShow) > 0;
    }

    @Override
    public List<Enterprise> getShowList() {
        return enterpriseMapper.getShowList();
    }
}
