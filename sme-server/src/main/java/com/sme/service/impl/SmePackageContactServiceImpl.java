package com.sme.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sme.dto.SmePackageContactPageQueryDTO;
import com.sme.entity.SmePackageContact;
import com.sme.mapper.SmePackageContactMapper;
import com.sme.result.PageResult;
import com.sme.service.SmePackageContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmePackageContactServiceImpl implements SmePackageContactService {

    @Autowired
    private SmePackageContactMapper smePackageContactMapper;

    /**
     * 分页查询
     * @param smePackageContactQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SmePackageContactPageQueryDTO smePackageContactQueryDTO) {
        PageHelper.startPage(smePackageContactQueryDTO.getPageNum(), smePackageContactQueryDTO.getPageSize());
        Page<SmePackageContact> page = smePackageContactMapper.pageQuery(smePackageContactQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }
}
