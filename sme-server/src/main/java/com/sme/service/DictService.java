package com.sme.service;

import com.github.pagehelper.PageInfo;
import com.sme.entity.Dict;
import com.sme.vo.DictVO;

public interface DictService {
    /**
     * 分页查询字典
     */
    PageInfo<Dict> getDictPage(Integer pageNum, Integer pageSize,
                               String dictCode, String dictName, Integer status);

    /**
     * 查询字典详情（含字典项）
     */
    DictVO getDictDetail(Long id);

    /**
     * 新增字典（含字典项）
     */
    boolean addDict(DictVO dictVO);

    /**
     * 修改字典（含字典项）
     */
    boolean updateDict(DictVO dictVO);

    /**
     * 删除字典（级联删除字典项）
     */
    boolean deleteDict(Long id);

    /**
     * 修改字典状态
     */
    boolean updateDictStatus(Long id, Integer status);

}

