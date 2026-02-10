package com.sme.service;

import com.sme.dto.SysDictDTO;
import com.sme.entity.SysDict;
import com.sme.result.Result;
import java.util.List;


public interface DictService {

    /**
     * 查询字典列表
     */
    List<SysDict> selectDictList();

    /**
     * 获取全部字典（下拉框）
     */
    Result<?> selectDictAll();

    /**
     * 根据 ID 查询字典
     */
    Result<?> selectDictById(Long id);

    /**
     * 新增字典
     */
    Result<?> insertDict(SysDictDTO dictDTO);

    /**
     * 修改字典
     */
    Result<?> updateDict(SysDictDTO dictDTO);

    /**
     * 修改字典状态
     */
    Result<?> updateDictStatus(Long id, Integer status);

    /**
     * 删除字典
     */
    Result<?> deleteDictByIds(Long[] ids);
}

