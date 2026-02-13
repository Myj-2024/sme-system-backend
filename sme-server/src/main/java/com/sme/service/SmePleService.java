package com.sme.service;

import com.sme.dto.SmePleQueryDTO;
import com.sme.entity.SmePLE;
import com.sme.result.PageResult;

public interface SmePleService {

    /**
     * 分页查询
     * @param smePleQueryDTO
     * @return
     */
    PageResult pageQuery(SmePleQueryDTO smePleQueryDTO);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SmePLE getPleById(Long id);

    /**
     * 新增
     * @param smePLE
     * @return
     */
    Boolean insert(SmePLE smePLE);

    /**
     * 修改
     * @param smePLE
     * @return
     */
    Boolean update(SmePLE smePLE);

    /**
     * 删除
     * @param id
     * @return
     */
    Boolean deleteById(Long id);
}
