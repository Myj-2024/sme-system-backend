package com.sme.service;

import com.sme.dto.IconPageQueryDTO;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.entity.Icon;


public interface IconService {

    /**
     * 分页查询图标
     * @param iconPageQueryDTO
     * @return
     */
    PageResult pageQuery(IconPageQueryDTO iconPageQueryDTO);

    /**
     * 根据ID查询图标详情
     * @param id
     * @return
     */
    Result<Icon> getById(Long id);

    /**
     * 新增图标
     * @param icon
     * @return
     */
    Result<Icon> add(Icon icon);

    /**
     * 修改图标
     * @param icon
     * @return
     */
    Result<Icon> update(Icon icon);

    /**
     * 删除图标
     * @param id
     * @return
     */
    Result<Void> delete(Long id);
}
