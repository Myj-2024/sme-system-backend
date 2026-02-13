package com.sme.service;

import com.sme.dto.SmePackageContactPageQueryDTO;
import com.sme.result.PageResult;

public interface SmePackageContactService {

    /**
     * 分页查询
     * @param smePackageContactQueryDTO
     * @return
     */
    PageResult pageQuery(SmePackageContactPageQueryDTO smePackageContactQueryDTO);
}
