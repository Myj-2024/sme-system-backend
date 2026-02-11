package com.sme.service;

import com.sme.dto.SysDictItemDTO;
import com.sme.dto.SysDictItemPageQueryDTO;
import com.sme.result.PageResult;
import com.sme.result.Result;
import com.sme.vo.SysDictItemVO;

import java.util.List;

public interface DictItemService {

    

    List<SysDictItemVO> selectItemsByDictCode(String dictCode);

    Result<?> insertItem(SysDictItemDTO itemDTO);

    Result<?> updateItem(SysDictItemDTO itemDTO);

    Result<?> deleteItemByIds(Long[] ids);

    PageResult selectItemList(SysDictItemPageQueryDTO sysDictItemPageQueryDTO);
}
