package com.sme.mapper;

import com.github.pagehelper.Page;
import com.sme.dto.SmePackageContactPageQueryDTO;
import com.sme.entity.SmePackageContact;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SmePackageContactMapper {

    /**
     * 分页查询
     * @param smePackageContactQueryDTO
     * @return
     */
    Page<SmePackageContact> pageQuery(SmePackageContactPageQueryDTO smePackageContactQueryDTO);
}
