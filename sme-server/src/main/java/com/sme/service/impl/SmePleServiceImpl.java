package com.sme.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sme.dto.SmePleQueryDTO;
import com.sme.entity.SmePLE;
import com.sme.mapper.SmePleMapper;
import com.sme.result.PageResult;
import com.sme.service.SmePleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmePleServiceImpl implements SmePleService {

    @Autowired
    private SmePleMapper smePleMapper;

    /**
     * 分页查询
     * @param smepleQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SmePleQueryDTO smepleQueryDTO) {
        PageHelper.startPage(smepleQueryDTO.getPageNum(), smepleQueryDTO.getPageSize());
        Page<SmePLE> page = smePleMapper.page(smepleQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public SmePLE getPleById(Long id) {
        return smePleMapper.getPleById(id);
    }

    /**
     * 新增
     * @param smePLE
     * @return
     */
    @Override
    public Boolean insert(SmePLE smePLE) {
        return smePleMapper.insert(smePLE) > 0;
    }

    /**
     * 修改
     * @param smePLE
     * @return
     */
    @Override
    public Boolean update(SmePLE smePLE) {
        return smePleMapper.update(smePLE) > 0;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    public Boolean deleteById(Long id) {
        return smePleMapper.deleteById(id) > 0;
    }

    /**
     * 检查企业是否被包抓联引用
     * @param enterpriseId
     * @return
     */
    @Override
    public boolean checkEnterpriseBind(Long enterpriseId) {
        Integer count = smePleMapper.countByEnterpriseId(enterpriseId);
        return count != null && count > 0;
    }

    /**
     * 检查部门是否被包抓联引用
     * @param deptId
     * @return
     */
    @Override
    public boolean checkDeptBind(Long deptId) {
        Integer count = smePleMapper.countByDeptId(deptId);
        return count != null && count > 0;
    }
}