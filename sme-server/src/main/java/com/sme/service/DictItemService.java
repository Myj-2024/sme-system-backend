package com.sme.service;

import com.sme.dto.SysDictItemDTO;
import com.sme.result.Result;
import com.sme.vo.SysDictItemVO;

import java.util.List;

public interface DictItemService {

    List<SysDictItemVO> selectItemList(SysDictItemVO itemVO);

    List<SysDictItemVO> selectItemsByDictCode(String dictCode);

    Result<?> insertItem(SysDictItemDTO itemDTO);

    Result<?> updateItem(SysDictItemDTO itemDTO);

    Result<?> deleteItemByIds(Long[] ids);
}
