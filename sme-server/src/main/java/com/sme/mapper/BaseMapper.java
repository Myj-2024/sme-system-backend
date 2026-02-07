package com.sme.mapper;

import java.util.List;


public interface BaseMapper<T> {

    /**
     * 查询列表
     */

    List<T> findAll();

    /**
     * 根据ID查询
     */
    T findById(Long id);

    /**
     * 新增
     */
    Boolean insert(T entity);

    /**
     * 修改
     */
    Boolean update(T entity);

    /**
     * 删除
     */
    Boolean deleteById(Long id);
}
